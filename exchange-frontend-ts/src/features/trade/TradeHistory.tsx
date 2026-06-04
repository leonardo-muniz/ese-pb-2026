import { ArrowDownLeft, ArrowUpRight, History, CheckCircle2 } from 'lucide-react';
import type { Order } from '../../types';

interface TradeHistoryProps {
  orders: Order[];
}

export const TradeHistory = ({ orders }: TradeHistoryProps) => {
  return (
    <section className="bg-slate-800/50 border border-slate-700/50 rounded-2xl backdrop-blur-sm overflow-hidden">
      <div className="p-6 border-b border-slate-700/50 flex items-center justify-between">
        <h3 className="text-lg font-bold flex items-center gap-2 text-white">
          <History size={20} className="text-indigo-400" />
          Histórico de Transações
        </h3>
      </div>
      
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm text-slate-300">
          <thead className="bg-slate-900/50 text-slate-400 text-xs uppercase tracking-wider">
            <tr>
              <th className="px-6 py-4 font-medium rounded-tl-lg">Ativo</th>
              <th className="px-6 py-4 font-medium">Tipo</th>
              <th className="px-6 py-4 font-medium text-right">Quantidade</th>
              <th className="px-6 py-4 font-medium text-right">Preço de Execução</th>
              <th className="px-6 py-4 font-medium text-right">Total (USD)</th>
              <th className="px-6 py-4 font-medium text-center">Data</th>
              <th className="px-6 py-4 font-medium text-center rounded-tr-lg">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-700/50">
            {orders.length === 0 ? (
              <tr>
                <td colSpan={7} className="px-6 py-12 text-center text-slate-500">
                  Nenhuma transação encontrada no seu histórico.
                </td>
              </tr>
            ) : (
              orders.map((order) => (
                <tr key={order.id} className="hover:bg-slate-700/20 transition-colors">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-2 font-bold text-white">
                      <div className="w-6 h-6 bg-orange-500 rounded-full flex items-center justify-center text-[10px] shadow-sm shadow-orange-500/30">₿</div>
                      {order.cryptoCurrency || 'BTC'}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`flex items-center gap-1.5 font-semibold ${order.type === 'BUY' ? 'text-emerald-400' : 'text-red-400'}`}>
                      {order.type === 'BUY' ? <ArrowDownLeft size={16} /> : <ArrowUpRight size={16} />}
                      {order.type === 'BUY' ? 'COMPRA' : 'VENDA'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right font-medium text-white">
                    {order.type === 'BUY' ? '+' : '-'}{order.amount}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    ${order.price.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right font-medium">
                    ${(order.amount * order.price).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center text-slate-400">
                    {new Date(order.createdAt).toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center flex justify-center">
                    <span className="flex items-center gap-1 bg-emerald-500/10 text-emerald-400 px-2 py-1 rounded-md text-xs font-semibold">
                      <CheckCircle2 size={14} />
                      {order.status === 'COMPLETED' ? 'Concluída' : order.status}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </section>
  );
};