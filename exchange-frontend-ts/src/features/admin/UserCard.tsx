import { Edit, Trash2, Wallet } from 'lucide-react';
import type { User } from '../../types';

interface UserCardProps {
  user: User;
  onEdit: () => void;
  onDelete: () => void;
}

export const UserCard = ({ user, onEdit, onDelete }: UserCardProps) => (
  <div className="p-5 border border-slate-100 rounded-2xl hover:border-indigo-100 hover:shadow-md transition-all group">
    <div className="flex justify-between items-start mb-3">
      <div className="w-10 h-10 rounded-full bg-indigo-50 text-indigo-600 flex items-center justify-center font-bold text-lg">
        {user.fullName ? user.fullName.charAt(0).toUpperCase() : '?'}
      </div>
      <div className="flex gap-2">
        <button onClick={onEdit} className="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors" title="Editar">
          <Edit size={16} />
        </button>
        <button onClick={onDelete} className="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors" title="Deletar">
          <Trash2 size={16} />
        </button>
      </div>
    </div>
    <span className="text-[10px] uppercase font-bold tracking-wider text-slate-400 bg-slate-50 px-2 py-1 rounded-md mb-2 inline-block">
      ID: {user.id ? user.id.substring(0, 8) : 'TEMP'}
    </span>
    <h3 className="font-semibold text-slate-800">{user.fullName}</h3>
    <p className="text-slate-500 text-sm truncate">{user.email}</p>
    <div className="mt-4 pt-4 border-t border-slate-50 flex items-center gap-2 text-sm font-medium text-emerald-600">
      <Wallet size={16} />
      <span>Carteira Pronta</span>
    </div>
  </div>
);