import { TrendingUp, TrendingDown } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import type { CoinMarketData } from '../../types';

interface MarketChartProps {
  coinData: CoinMarketData | null;
  chartData: any[];
  loading: boolean;
  error: boolean;
}

export const MarketChart = ({ coinData, chartData, loading, error }: MarketChartProps) => {
  const isPositive = coinData && coinData.priceChangePercentage24h >= 0;

  return (
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
  );
};