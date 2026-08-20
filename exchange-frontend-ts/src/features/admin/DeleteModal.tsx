import { AlertTriangle } from 'lucide-react';

interface DeleteModalProps {
  isOpen: boolean;
  onCancel: () => void;
  onConfirm: () => void;
}

export const DeleteModal = ({ isOpen, onCancel, onConfirm }: DeleteModalProps) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4" role="dialog" aria-modal="true" aria-labelledby="modal-title">
      <div className="bg-white rounded-2xl p-6 w-full max-w-sm shadow-xl">
        <div className="flex items-center gap-3 text-red-600 mb-3">
          <AlertTriangle size={24} />
          <h3 id="modal-title" className="text-lg font-bold">Confirmar Exclusão</h3>
        </div>
        <p className="text-slate-600 mb-6 text-sm">
          Tem certeza que deseja excluir este usuário? Esta ação é permanente e não poderá ser desfeita.
        </p>
        <div className="flex gap-3 justify-end">
          <button onClick={onCancel} className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl transition-colors font-medium text-sm">
            Cancelar
          </button>
          <button onClick={onConfirm} className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-xl transition-colors font-medium shadow-md shadow-red-200 text-sm">
            Sim, Excluir
          </button>
        </div>
      </div>
    </div>
  );
};