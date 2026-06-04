import { useState, useEffect, useCallback } from 'react';
import type { Wallet } from '../types';

const API_URL = import.meta.env?.VITE_API_URL 
  ? `${import.meta.env.VITE_API_URL}/wallets` 
  : 'http://localhost:8080/api/v1/wallets';

// ID do usuário logado (Mock)
const MOCK_USER_ID = '12345678-1234-1234-1234-123456789abc';

// Estado inicial do Sandbox
const INITIAL_MOCK: Wallet[] = [
  { id: crypto.randomUUID(), userId: MOCK_USER_ID, currency: 'USD', balance: 15000.00 },
  { id: crypto.randomUUID(), userId: MOCK_USER_ID, currency: 'BTC', balance: 0.5 }
];

let mockDatabase: Wallet[] = [...INITIAL_MOCK];

export function useWallet(userId: string = MOCK_USER_ID) {
  const [wallets, setWallets] = useState<Wallet[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [isMockMode, setIsMockMode] = useState<boolean>(false);

  const fetchWallets = useCallback(async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/user/${userId}`);
      if (!response.ok) throw new Error('Falha ao buscar carteiras');
      
      const data: Wallet[] = await response.json();
      setWallets(data);
      setIsMockMode(false);
    } catch (error) {
      // Sandbox: Filtra as carteiras do usuário atual no banco em memória
      const userWallets = mockDatabase.filter(w => w.userId === userId);
      setWallets([...userWallets]);
      setIsMockMode(true);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchWallets();
  }, [fetchWallets]);

  // Função auxiliar para atualizar o saldo apenas no Sandbox, 
  // já que na API real o backend (WalletService) debita automaticamente.
  const updateMockBalance = (currency: string, amount: number, operation: 'ADD' | 'SUBTRACT') => {
    const index = mockDatabase.findIndex(w => w.userId === userId && w.currency === currency);
    if (index > -1) {
      if (operation === 'ADD') mockDatabase[index].balance += amount;
      else mockDatabase[index].balance -= amount;
      fetchWallets();
    }
  };

  return { wallets, loading, isMockMode, fetchWallets, updateMockBalance };
}