import { useState, useEffect, useCallback } from 'react';
import type { User, UserFormData, AlertMessage } from '../types';

const API_URL = import.meta.env?.VITE_API_URL || 'http://localhost:8080/api/v1/users';

const INITIAL_MOCK: User[] = [
  { id: '12345678-1234-1234-1234-123456789abc', fullName: 'Satoshi Nakamoto', email: 'satoshi@bitcoin.com' }
];

let mockDatabase: User[] = [...INITIAL_MOCK];

export function useUsers() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [message, setMessage] = useState<AlertMessage>({ type: '', text: '' });
  const [editingId, setEditingId] = useState<string | null>(null);
  const [userToDelete, setUserToDelete] = useState<string | null>(null);
  const [isMockMode, setIsMockMode] = useState<boolean>(false);

  const [formData, setFormData] = useState<UserFormData>({
    fullName: '',
    email: '',
    password: ''
  });

  const fetchUsers = useCallback(async (isManualRefresh: boolean = false) => {
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Falha ao buscar dados');
      
      const data: User[] = await response.json();
      setUsers(data);
      setIsMockMode(false);
    } catch (error) {
      if (isManualRefresh) {
        mockDatabase = [...INITIAL_MOCK];
        setMessage({ type: 'success', text: 'Simulador local restaurado ao padrão inicial.' });
      }
      
      setUsers([...mockDatabase]);
      setIsMockMode(true);
    }
  }, []);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const startEditing = (user: User) => {
    setEditingId(user.id || null);
    setFormData({ fullName: user.fullName, email: user.email, password: '' });
    setMessage({ type: '', text: '' });
  };

  const resetForm = useCallback(() => {
    setEditingId(null);
    setFormData({ fullName: '', email: '', password: '' });
  }, []);

  const cancelEditing = () => {
    resetForm();
    setMessage({ type: '', text: '' });
  };

  const clearFormIfEditingDeleted = useCallback((id: string) => {
    if (id === editingId) resetForm();
  }, [editingId, resetForm]);

  const requestDelete = (id: string | null) => {
    setUserToDelete(id);
  };

  const clearMessage = useCallback(() => {
    setMessage({ type: '', text: '' });
  }, []);

  const executeDelete = async () => {
    if (!userToDelete) return;
    const id = userToDelete;
    setUserToDelete(null); 
    
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (response.ok) {
        clearFormIfEditingDeleted(id);
        setMessage({ type: 'success', text: 'Usuário removido com sucesso!' });
        fetchUsers();
      } else {
        throw new Error('Erro na deleção');
      }
    } catch (error) {
      mockDatabase = mockDatabase.filter(u => u.id !== id);
      setUsers([...mockDatabase]);
      clearFormIfEditingDeleted(id);
      setIsMockMode(true);
      setMessage({ type: 'success', text: 'Usuário removido do ambiente de testes local.' });
    }
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const isEditing = !!editingId;

    // VERIFICAÇÃO DE ALTERAÇÕES
    // Se o usuário clicar em salvar sem mudar nome, e-mail ou inserir nova senha, sai silenciosamente
    if (isEditing) {
      const originalUser = users.find(u => u.id === editingId);
      const hasChanges = 
        formData.fullName !== originalUser?.fullName ||
        formData.email !== originalUser?.email ||
        (formData.password !== undefined && formData.password.trim() !== '');

      if (!hasChanges) {
        cancelEditing();
        return;
      }
    }

    setLoading(true);
    setMessage({ type: '', text: '' });

    const url = isEditing ? `${API_URL}/${editingId}` : API_URL;
    const method = isEditing ? 'PUT' : 'POST';
    
    const payload: UserFormData = { ...formData };
    
    if (isEditing && (!payload.password || payload.password.trim() === '')) {
      delete payload.password;
    }

    try {
      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        resetForm();
        setMessage({ type: 'success', text: `Usuário ${isEditing ? 'atualizado' : 'cadastrado'} com sucesso!` });
        fetchUsers(); 
        setIsMockMode(false);
      } else {
        const errorData: Record<string, string> = await response.json();
        const errorMessages = Object.values(errorData).join(' | ');
        setMessage({ type: 'error', text: errorMessages || 'Erro ao salvar.' });
      }
    } catch (error) {
      if (isEditing) {
        const index = mockDatabase.findIndex(u => u.id === editingId);
        if (index > -1) {
          const updatedUser = { ...formData };
          if (!updatedUser.password) delete updatedUser.password;
          mockDatabase[index] = { ...mockDatabase[index], ...updatedUser };
        }
      } else {
        mockDatabase.push({ id: crypto.randomUUID(), ...formData });
      }
      
      setUsers([...mockDatabase]);
      setIsMockMode(true);
      resetForm();
      setMessage({ type: 'success', text: `Usuário ${isEditing ? 'atualizado' : 'cadastrado'} no ambiente de testes local.` });
    } finally {
      setLoading(false);
    }
  };

  return {
    users, loading, message, formData, editingId, userToDelete, isMockMode,
    requestDelete, setUserToDelete, handleInputChange, startEditing, cancelEditing,
    executeDelete, handleSubmit, fetchUsers, clearMessage
  };
}