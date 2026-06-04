import React, { useState } from 'react';
import { Activity, TrendingUp, TrendingDown, ArrowRightLeft, DollarSign, User as UserIcon, AlertTriangle } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import { useCoinGecko } from '../../hooks/useCoinGecko';
import { useWallet } from '../../hooks/useWallet';
import { useTrade } from '../../hooks/useTrade';
import { Toast } from '../../components/Toast';
import { TradeHistory } from '../../features/trade/TradeHistory';
import type { AlertMessage } from '../../types';

export const TradingApp = () => {
  const { coinData, chartData, loading, error } = useCoinGecko('bitcoin');
  
  const { wallets, fetchWallets, updateMockBalance, isMockMode: isWalletMock } = useWallet();
  const { createOrder, orders, loading: isTradeLoading, isMockMode: isTradeMock } = useTrade();

  const [tradeType, setTradeType] = useState<'BUY' | 'SELL'>('BUY');
  const [amount, setAmount] = useState<string>('');
  const [message, setMessage] = useState<AlertMessage>({ type: '', text: '' });
  const [isConfirming, setIsConfirming] = useState<boolean>(false);

  const usdWallet = wallets.find(w => w.currency === 'USD');
  const btcWallet = wallets.find(w => w.currency === 'BTC');
  const fiatBalance = usdWallet?.balance || 0;
  const cryptoBalance = btcWallet?.balance || 0;

  const isPositive = coinData && coinData.priceChangePercentage24h >= 0;
  const totalEstimated = (parseFloat(amount) || 0) * (coinData?.currentPrice || 0);

  const handlePreSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const numAmount = parseFloat(amount);
    
    if (!numAmount || numAmount <= 0) {
      setMessage({ type: 'error', text: 'Insira uma quantidade válida.' });
      return;
    }
    
    if (tradeType === 'BUY' && totalEstimated > fiatBalance) {
      setMessage({ type: 'error', text: 'Saldo insuficiente em USD para realizar esta compra.' });
      return;
    }
    
    if (tradeType === 'SELL' && numAmount > cryptoBalance) {
      setMessage({ type: 'error', text: 'Saldo insuficiente em BTC para realizar esta venda.' });
      return;
    }

    setIsConfirming(true);
  };

  const executeTrade = async () => {
    const numAmount = parseFloat(amount);
    
    const result = await createOrder(tradeType, 'BTC', numAmount, coinData!.currentPrice);
    
    if (result.success) {
      if (isTradeMock || isWalletMock) {
        if (tradeType === 'BUY') {
          updateMockBalance('USD', totalEstimated, 'SUBTRACT');
          updateMockBalance('BTC', numAmount, 'ADD');
        } else {
          updateMockBalance('USD', totalEstimated, 'ADD');
          updateMockBalance('BTC', numAmount, 'SUBTRACT');
        }
      } else {
        fetchWallets();
      }
      
      setIsConfirming(false);
      setAmount('');
      setMessage({ type: 'success', text: result.message });
    } else {
      setIsConfirming(false);
      setMessage({ type: 'error', text: 'Falha ao executar ordem.' });
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 font-sans p-6 relative">
      <Toast message={message} onClose={() => setMessage({ type: '', text: '' })} />

      {isConfirming && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/70 backdrop-blur-sm p-4">
          <div className="bg-slate-800 rounded-2xl p-6 w-full max-w-sm shadow-2xl border border-slate-700">
            <div className="flex items-center gap-3 text-amber-500 mb-4">
              <AlertTriangle size={24} />
              <h3 className="text-lg font-bold text-white">Confirmar Transação</h3>
            </div>
            
            <div className="bg-slate-900 p-4 rounded-xl mb-6 space-y-3 text-sm">
              <div className="flex justify-between">
                <span className="text-slate-400">Operação</span>
                <span className={`font-bold ${tradeType === 'BUY' ? 'text-emerald-400' : 'text-red-400'}`}>
                  {tradeType === 'BUY' ? 'COMPRA' : 'VENDA'} DE BTC
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-400">Quantidade</span>
                <span className="font-semibold text-white">{amount} BTC</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-400">Preço Unitário</span>
                <span className="font-semibold text-white">${coinData?.currentPrice.toLocaleString()}</span>
              </div>
              <div className="h-px w-full bg-slate-800 my-2"></div>
              <div className="flex justify-between">
                <span className="text-slate-400">Total {tradeType === 'BUY' ? 'a Pagar' : 'a Receber'}</span>
                <span className="font-bold text-white">${totalEstimated.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
              </div>
            </div>

            <div className="flex gap-3 justify-end">
              <button
                disabled={isTradeLoading}
                onClick={() => setIsConfirming(false)}
                className="px-4 py-2.5 bg-slate-700 hover:bg-slate-600 text-white rounded-xl transition-colors font-medium text-sm disabled:opacity-50"
              >
                Cancelar
              </button>
              <button
                disabled={isTradeLoading}
                onClick={executeTrade}
                className={`px-4 py-2.5 text-white rounded-xl transition-colors font-bold shadow-lg text-sm flex-1 disabled:opacity-50 ${
                  tradeType === 'BUY' ? 'bg-emerald-600 hover:bg-emerald-500 shadow-emerald-900/50' : 'bg-red-600 hover:bg-red-500 shadow-red-900/50'
                }`}
              >
                {isTradeLoading ? 'Processando...' : 'Sim, Confirmar'}
              </button>
            </div>
          </div>
        </div>
      )}

      <header className="max-w-7xl mx-auto mb-8 flex items-center justify-between border-b border-slate-800 pb-6">
        <div className="flex items-center gap-3">
          <div className="bg-indigo-600 p-2.5 rounded-xl text-white shadow-lg shadow-indigo-900/50">
            <Activity size={24} />
          </div>
          <div>
            <h1 className="text-xl font-bold text-white">Crypto Trading</h1>
            <p className="text-slate-400 text-xs tracking-wider uppercase">Plataforma de Investimento</p>
          </div>
        </div>
        
        <div className="flex items-center gap-6 text-sm">
          <div className="hidden md:flex items-center gap-3 bg-slate-800 py-1.5 pl-2 pr-4 rounded-full border border-slate-700">
            <div className="bg-indigo-500/20 p-1.5 rounded-full text-indigo-400">
              <UserIcon size={16} />
            </div>
            <span className="text-sm font-medium text-slate-200">Leonardo Muniz</span>
          </div>

          <div className="h-8 w-px bg-slate-800 hidden md:block"></div>

          <div className="text-right">
            <p className="text-slate-400">Saldo USD</p>
            <p className="font-bold text-white">${fiatBalance.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</p>
          </div>
          <div className="h-8 w-px bg-slate-800"></div>
          <div className="text-right">
            <p className="text-slate-400">Saldo BTC</p>
            <p className="font-bold text-white">{cryptoBalance.toFixed(4)} BTC</p>
          </div>
        </div>
      </header>

      {/* MAIN AJUSTADA PARA DIVIDIR A TELA COM O HISTÓRICO */}
      <main className="max-w-7xl mx-auto space-y-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          
          <section className="lg:col-span-2 space-y-6">
            <div className="bg-slate-800/50 border border-slate-700/50 p-6 rounded-2xl backdrop-blur-sm">
              <div className="flex justify-between items-start mb-6">
                <div>
                  <div className="flex items-center gap-3 mb-2">
                    <div className="w-10 h-10 bg-orange-500 rounded-full flex items-center justify-center font-bold text-white shadow-lg shadow-orange-500/30">₿</div>
                    <h2 className="text-2xl font-bold">Bitcoin <span className="text-slate-400 text-lg font-medium">BTC</span></h2>
                  </div>
                  {loading ? (
                    <div className="h-10 w-48 bg-slate-700 animate-pulse rounded-lg mt-4"></div>
                  ) : error ? (
                    <p className="text-red-400 mt-2">Falha ao carregar dados do mercado.</p>
                  ) : (
                    <div className="flex items-baseline gap-4 mt-2">
                      <span className="text-4xl font-bold text-white">
                        ${coinData?.currentPrice.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                      </span>
                      <span className={`flex items-center gap-1 font-semibold px-2.5 py-1 rounded-lg text-sm ${isPositive ? 'bg-emerald-500/10 text-emerald-400' : 'bg-red-500/10 text-red-400'}`}>
                        {isPositive ? <TrendingUp size={16} /> : <TrendingDown size={16} />}
                        {Math.abs(coinData?.priceChangePercentage24h || 0).toFixed(2)}%
                      </span>
                    </div>
                  )}
                </div>
              </div>
              <div className="h-72 w-full mt-4">
                {loading || chartData.length === 0 ? (
                  <div className="w-full h-full bg-slate-800 animate-pulse rounded-xl"></div>
                ) : (
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={chartData} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                      <defs>
                        <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3}/>
                          <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                        </linearGradient>
                      </defs>
                      <XAxis dataKey="time" stroke="#475569" fontSize={12} tickLine={false} axisLine={false} minTickGap={30} />
                      <YAxis domain={['auto', 'auto']} stroke="#475569" fontSize={12} tickLine={false} axisLine={false} tickFormatter={(val) => `$${val.toLocaleString()}`} />
                      <Tooltip 
                        contentStyle={{ backgroundColor: '#1e293b', border: '1px solid #334155', borderRadius: '12px', color: '#f8fafc' }}
                        itemStyle={{ color: '#818cf8', fontWeight: 'bold' }}
                        labelStyle={{ color: '#94a3b8', marginBottom: '4px' }}
                        formatter={(value: any) => `$${Number(value).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
                      />
                      <Area type="monotone" dataKey="price" name="Preço" stroke="#818cf8" strokeWidth={2} fillOpacity={1} fill="url(#colorPrice)" />
                    </AreaChart>
                  </ResponsiveContainer>
                )}
              </div>
            </div>
          </section>

          <section className="bg-slate-800/50 border border-slate-700/50 p-6 rounded-2xl backdrop-blur-sm h-fit">
            <div className="flex items-center justify-between mb-6">
              <h3 className="text-lg font-bold flex items-center gap-2">
                <ArrowRightLeft size={20} className="text-indigo-400" />
                Nova Ordem
              </h3>
            </div>

            <div className="flex p-1 bg-slate-900 rounded-xl mb-6">
              <button 
                onClick={() => { setTradeType('BUY'); setAmount(''); }}
                className={`flex-1 py-2 rounded-lg font-semibold text-sm transition-all ${tradeType === 'BUY' ? 'bg-emerald-500 text-white shadow-lg shadow-emerald-500/25' : 'text-slate-400 hover:text-white'}`}
              >
                Comprar
              </button>
              <button 
                onClick={() => { setTradeType('SELL'); setAmount(''); }}
                className={`flex-1 py-2 rounded-lg font-semibold text-sm transition-all ${tradeType === 'SELL' ? 'bg-red-500 text-white shadow-lg shadow-red-500/25' : 'text-slate-400 hover:text-white'}`}
              >
                Vender
              </button>
            </div>

            <form className="space-y-5" onSubmit={handlePreSubmit}>
              <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Preço Atual (USD)</label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <DollarSign size={18} className="text-slate-500" />
                  </div>
                  <input
                    type="text"
                    readOnly
                    value={coinData?.currentPrice.toLocaleString() || '---'}
                    className="w-full pl-10 pr-4 py-3 bg-slate-900 border border-slate-700 rounded-xl text-white font-medium focus:outline-none opacity-70 cursor-not-allowed"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-400 mb-1">Quantidade (BTC)</label>
                <input
                  type="number"
                  step="0.0001"
                  min="0.0001"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="Ex: 0.05"
                  className="w-full px-4 py-3 bg-slate-900 border border-slate-700 rounded-xl text-white focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 outline-none transition-all [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                />
              </div>

              <div className="pt-2">
                <div className="flex justify-between text-sm mb-4">
                  <span className="text-slate-400">Total Estimado</span>
                  <span className="font-bold text-white">${totalEstimated.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
                </div>
                <button 
                  type="submit"
                  disabled={!coinData || isConfirming}
                  className={`w-full py-3.5 rounded-xl font-bold text-white transition-all disabled:opacity-50 disabled:cursor-not-allowed ${
                    tradeType === 'BUY' ? 'bg-emerald-600 hover:bg-emerald-500 shadow-lg shadow-emerald-900/50' : 'bg-red-600 hover:bg-red-500 shadow-lg shadow-red-900/50'
                  }`}
                >
                  {tradeType === 'BUY' ? 'Executar Compra' : 'Executar Venda'}
                </button>
              </div>
            </form>
          </section>

        </div>

        {/* COMPONENTE DE HISTÓRICO */}
        <TradeHistory orders={orders} />
      </main>
    </div>
  );
};