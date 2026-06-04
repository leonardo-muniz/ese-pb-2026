import { useState, useEffect } from 'react';
import { Activity, LogOut, Server } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const API_URL = import.meta.env?.VITE_API_URL || 'http://localhost:8080/api/v1/users';

export const Header = () => {
  const navigate = useNavigate();
  const [isApiOnline, setIsApiOnline] = useState<boolean>(false);

  useEffect(() => {
    const checkApiStatus = async () => {
      try {
        const response = await fetch(API_URL, { method: 'GET' });
        setIsApiOnline(response.ok);
      } catch (error) {
        setIsApiOnline(false);
      }
    };

    checkApiStatus();
    const intervalId = setInterval(checkApiStatus, 30000);

    return () => clearInterval(intervalId);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('isAuthenticated');
    navigate('/admin/login');
  };

  return (
    <header className="max-w-6xl mx-auto mb-10 flex items-center justify-between">
      <div className="flex items-center gap-3">
        <div className="bg-indigo-600 p-3 rounded-xl text-white shadow-lg shadow-indigo-200">
          <Activity size={28} />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Crypto Exchange Admin</h1>
          <p className="text-slate-500 text-sm">Gerenciamento do TP1 - Monólito Modular</p>
        </div>
      </div>

      <div className="flex items-center gap-3">
        <div 
          className={`flex items-center gap-2 px-3 py-2 rounded-xl border text-sm font-medium transition-all ${
            isApiOnline 
              ? 'bg-emerald-50 border-emerald-200 text-emerald-700 shadow-sm shadow-emerald-100' 
              : 'bg-red-50 border-red-200 text-red-700 shadow-sm shadow-red-100'
          }`}
          title={isApiOnline ? "Conexão estabelecida com sucesso" : "Falha ao conectar com o servidor"}
        >
          <div className="relative flex h-2.5 w-2.5">
            {isApiOnline && (
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
            )}
            <span className={`relative inline-flex rounded-full h-2.5 w-2.5 ${isApiOnline ? 'bg-emerald-500' : 'bg-red-500'}`}></span>
          </div>
          <Server size={16} className={isApiOnline ? 'text-emerald-600' : 'text-red-600'} />
          <span className="hidden sm:inline">{isApiOnline ? 'API Online' : 'API Offline'}</span>
        </div>

        <button 
          onClick={handleLogout} 
          className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-slate-500 hover:text-red-600 bg-white border border-slate-200 hover:border-red-100 hover:bg-red-50 rounded-xl transition-all"
        >
          <LogOut size={16} />
          <span className="hidden sm:inline">Sair do Painel</span>
        </button>
      </div>
    </header>
  );
};