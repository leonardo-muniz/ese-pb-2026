import { AlertTriangle } from 'lucide-react';

interface ConfirmOrderModalProps {
  tradeType: 'BUY' | 'SELL';
  amount: string;
  coinPrice: number;
  totalEstimated: number;
  isTradeLoading: boolean;
  onCancel: () => void;
  onConfirm: () => void;
}

export const ConfirmOrderModal = ({ tradeType, amount, coinPrice, totalEstimated, isTradeLoading, onCancel, onConfirm }: ConfirmOrderModalProps) => (
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
          <span className="font-semibold text-white">${coinPrice.toLocaleString()}</span>
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
          onClick={onCancel}
          className="px-4 py-2.5 bg-slate-700 hover:bg-slate-600 text-white rounded-xl transition-colors font-medium text-sm disabled:opacity-50"
        >
          Cancelar
        </button>
        <button
          disabled={isTradeLoading}
          onClick={onConfirm}
          className={`px-4 py-2.5 text-white rounded-xl transition-colors font-bold shadow-lg text-sm flex-1 disabled:opacity-50 ${
            tradeType === 'BUY' ? 'bg-emerald-600 hover:bg-emerald-500 shadow-emerald-900/50' : 'bg-red-600 hover:bg-red-500 shadow-red-900/50'
          }`}
        >
          {isTradeLoading ? 'Processando...' : 'Sim, Confirmar'}
        </button>
      </div>
    </div>
  </div>
);