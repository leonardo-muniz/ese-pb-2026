import { User as UserIcon } from 'lucide-react';

interface WalletHeaderProps {
  fiatBalance: number;
  cryptoBalance: number;
  userName?: string;
}

export const WalletHeader = ({ fiatBalance, cryptoBalance, userName = 'Leonardo Muniz' }: WalletHeaderProps) => (
  <div className="flex items-center gap-6 text-sm">
    <div className="hidden md:flex items-center gap-3 bg-slate-800 py-1.5 pl-2 pr-4 rounded-full border border-slate-700">
      <div className="bg-indigo-500/20 p-1.5 rounded-full text-indigo-400">
        <UserIcon size={16} />
      </div>
      <span className="text-sm font-medium text-slate-200">{userName}</span>
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
);