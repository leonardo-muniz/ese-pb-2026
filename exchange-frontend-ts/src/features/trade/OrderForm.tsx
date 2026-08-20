import React from 'react';
import { ArrowRightLeft, DollarSign } from 'lucide-react';
import type { CoinMarketData } from '../../types';

interface OrderFormProps {
  coinData: CoinMarketData | null;
  tradeType: 'BUY' | 'SELL';
  setTradeType: (type: 'BUY' | 'SELL') => void;
  amount: string;
  setAmount: (amount: string) => void;
  totalEstimated: number;
  isConfirming: boolean;
  onSubmit: (e: React.FormEvent) => void;
}

export const OrderForm = ({ coinData, tradeType, setTradeType, amount, setAmount, totalEstimated, isConfirming, onSubmit }: OrderFormProps) => (
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

    <form className="space-y-5" onSubmit={onSubmit}>
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
);