import { AlertCircle, CheckCircle2 } from 'lucide-react';
import type { AlertMessage } from '../types';

export const AlertBox = ({ message }: { message: AlertMessage }) => {
  if (!message.text) return null;
  const isError = message.type === 'error';
  return (
    <div className={`p-4 rounded-lg mb-6 flex items-start gap-3 text-sm ${isError ? 'bg-red-50 text-red-700' : 'bg-emerald-50 text-emerald-700'}`}>
      {isError ? <AlertCircle size={20} className="shrink-0 mt-0.5" /> : <CheckCircle2 size={20} className="shrink-0 mt-0.5" />}
      <p>{message.text}</p>
    </div>
  );
};