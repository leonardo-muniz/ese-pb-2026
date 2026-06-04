import { useState, useCallback, useEffect } from 'react';
import type { Order, OrderType } from '../types';

const API_URL = import.meta.env?.VITE_API_URL 
  ? `${import.meta.env.VITE_API_URL}/orders` 
  : 'http://localhost:8080/api/v1/orders';

const MOCK_USER_ID = '12345678-1234-1234-1234-123456789abc';
let mockDatabase: Order[] = [];

export function useTrade(userId: string = MOCK_USER_ID) {
  const [loading, setLoading] = useState<boolean>(false);
  const [isMockMode, setIsMockMode] = useState<boolean>(false);
  const [orders, setOrders] = useState<Order[]>([]);

  const fetchOrders = useCallback(async () => {
    try {
      const response = await fetch(`${API_URL}/user/${userId}`);
      if (!response.ok) throw new Error('Falha ao buscar histórico de ordens');
      
      const data: Order[] = await response.json();
      setOrders(data);
      setIsMockMode(false);
    } catch (error) {
      // Sandbox: Ordena do mais recente para o mais antigo
      const userOrders = mockDatabase
        .filter(o => o.userId === userId)
        .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      
      setOrders([...userOrders]);
      setIsMockMode(true);
    }
  }, [userId]);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  const createOrder = async (
    type: OrderType,
    cryptoCurrency: string,
    amount: number,
    price: number
  ): Promise<{ success: boolean; message: string }> => {
    setLoading(true);
    const payload = { userId, type, cryptoCurrency, amount, price };

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        setIsMockMode(false);
        fetchOrders(); // Recarrega os dados reais
        return { success: true, message: `Ordem de ${type === 'BUY' ? 'compra' : 'venda'} executada com sucesso!` };
      } else {
        const errorData = await response.json();
        throw new Error(Object.values(errorData).join(' | ') || 'Erro ao processar ordem.');
      }
    } catch (error) {
      const newOrder: Order = {
        id: crypto.randomUUID(),
        userId,
        type,
        cryptoCurrency,
        amount,
        price,
        status: 'COMPLETED',
        createdAt: new Date().toISOString()
      };
      mockDatabase.push(newOrder);
      setIsMockMode(true);
      fetchOrders(); // Recarrega os dados do simulador
      
      return { 
        success: true, 
        message: `Sandbox: Ordem de ${type === 'BUY' ? 'compra' : 'venda'} executada localmente.` 
      };
    } finally {
      setLoading(false);
    }
  };

  return { createOrder, orders, fetchOrders, loading, isMockMode };
}