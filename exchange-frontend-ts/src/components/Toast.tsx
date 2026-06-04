import { useEffect } from 'react';
import { CheckCircle2, AlertCircle, X } from 'lucide-react';
import type { AlertMessage } from '../types';

interface ToastProps {
  message: AlertMessage;
  onClose: () => void;
}

export const Toast = ({ message, onClose }: ToastProps) => {
  useEffect(() => {
    if (message.text) {
      const timer = setTimeout(() => {
        onClose();
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [message, onClose]);

  if (!message.text) return null;

  const isSuccess = message.type === 'success';

  return (
    <div className="fixed bottom-6 right-6 z-50 animate-fade-in-up">
      <div className={`flex items-center gap-3 px-5 py-4 rounded-2xl shadow-2xl border backdrop-blur-md ${
        isSuccess 
          ? 'bg-emerald-950/90 border-emerald-800/50 text-emerald-50 shadow-emerald-900/20' 
          : 'bg-red-950/90 border-red-800/50 text-red-50 shadow-red-900/20'
      }`}>
        <div className={isSuccess ? 'text-emerald-400' : 'text-red-400'}>
          {isSuccess ? <CheckCircle2 size={24} /> : <AlertCircle size={24} />}
        </div>
        
        <p className="font-medium text-sm pr-6">{message.text}</p>
        
        <button 
          onClick={onClose}
          className={`p-1.5 rounded-lg transition-colors border border-transparent ${
            isSuccess 
              ? 'hover:bg-emerald-900/50 text-emerald-400 hover:text-emerald-200 hover:border-emerald-800/50' 
              : 'hover:bg-red-900/50 text-red-400 hover:text-red-200 hover:border-red-800/50'
          }`}
        >
          <X size={16} strokeWidth={2.5} />
        </button>
      </div>
    </div>
  );
};