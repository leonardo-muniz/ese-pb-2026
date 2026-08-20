import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Login } from './pages/admin/Login'; // Ajuste o import conforme a nova estrutura
import { Dashboard } from './pages/admin/Dashboard'; // Ajuste o import
import { TradingApp } from './pages/client/TradingApp'; // NOVO import

const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';
  return isAuthenticated ? children : <Navigate to="/admin/login" replace />;
};

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rotas Administrativas */}
        <Route path="/admin/login" element={<Login />} />
        <Route 
          path="/admin" 
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          } 
        />
        
        {/* Rota do Cliente Final (Trade & Wallet) */}
        <Route path="/app" element={<TradingApp />} />
        
        {/* Redirecionamento padrão da raiz para o app do cliente */}
        <Route path="/" element={<Navigate to="/app" replace />} />
        <Route path="*" element={<Navigate to="/app" replace />} />
      </Routes>
    </BrowserRouter>
  );
}