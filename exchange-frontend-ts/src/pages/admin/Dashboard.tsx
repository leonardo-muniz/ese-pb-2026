import { UserPlus, Users, ServerOff, SearchX, DatabaseBackup } from 'lucide-react';
import { useUsers } from '../../hooks/useUsers';
import { Header } from '../../components/Header';
import { Toast } from '../../components/Toast';
import { DeleteModal } from '../../features/admin/DeleteModal';
import { UserCard } from '../../features/admin/UserCard';

export const Dashboard = () => {
  const {
    users, loading, message, formData, editingId, userToDelete, isMockMode,
    requestDelete, setUserToDelete, handleInputChange, startEditing, cancelEditing,
    executeDelete, handleSubmit, fetchUsers, clearMessage
  } = useUsers();

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 font-sans p-6 relative overflow-x-hidden">
      
      <DeleteModal isOpen={!!userToDelete} onCancel={() => setUserToDelete(null)} onConfirm={executeDelete} />
      
      <Toast message={message} onClose={clearMessage} />
      
      <Header />

      <main className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Formulário */}
        <section className="lg:col-span-1 bg-white p-6 rounded-2xl shadow-sm border border-slate-100 h-fit">
          <div className="flex items-center gap-2 mb-6 pb-4 border-b border-slate-100">
            <UserPlus className="text-indigo-500" size={24} />
            <h2 className="text-lg font-semibold">{editingId ? 'Editar Usuário' : 'Novo Usuário'}</h2>
          </div>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Nome Completo</label>
              <input
                type="text" name="fullName" value={formData.fullName} onChange={handleInputChange} required
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:outline-none transition-all"
                placeholder="Ex: Satoshi Nakamoto"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">E-mail</label>
              <input
                type="email" name="email" value={formData.email} onChange={handleInputChange} required
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:outline-none transition-all"
                placeholder="satoshi@bitcoin.com"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Senha Segura</label>
              <input
                type="password" name="password" value={formData.password} onChange={handleInputChange} required={!editingId} minLength={6}
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:outline-none transition-all"
                placeholder={editingId ? "Deixe em branco para manter a atual" : "Mínimo de 6 caracteres"}
              />
            </div>

            <div className="flex gap-2">
              <button 
                type="submit" 
                disabled={loading} 
                className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-xl transition-colors shadow-md shadow-indigo-200 disabled:opacity-50"
              >
                {loading ? 'Salvando...' : (editingId ? 'Atualizar Dados' : 'Cadastrar na Plataforma')}
              </button>
              
              {editingId && (
                <button type="button" onClick={cancelEditing} className="px-4 bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium rounded-xl transition-colors">
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </section>

        {/* Lista de Usuários */}
        <section className="lg:col-span-2 bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
          <div className="flex items-center justify-between mb-6 pb-4 border-b border-slate-100">
            <div className="flex items-center gap-2">
              <Users className="text-indigo-500" size={24} />
              
              <div className="flex items-center gap-3">
                <h2 className="text-lg font-semibold">Usuários Ativos na Rede</h2>
                {isMockMode && (
                  <span className="flex items-center gap-1.5 px-2.5 py-1 bg-amber-100 text-amber-700 text-[11px] font-bold uppercase tracking-wider rounded-lg border border-amber-200" title="Suas alterações não estão sendo enviadas ao servidor">
                    <ServerOff size={14} />
                    Sandbox Local
                  </span>
                )}
              </div>
            </div>
            
            <button onClick={() => fetchUsers(true)} type="button" className="text-sm font-medium text-indigo-600 hover:text-indigo-800 transition-colors">
              {isMockMode ? 'Resetar Lista' : 'Atualizar Lista'}
            </button>
          </div>

          {users.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 px-4 bg-slate-50/50 border-2 border-dashed border-slate-200 rounded-2xl text-center transition-all">
              {isMockMode ? (
                <>
                  <div className="bg-amber-100 text-amber-600 p-4 rounded-full mb-4 shadow-sm">
                    <DatabaseBackup size={32} strokeWidth={1.5} />
                  </div>
                  <h3 className="text-lg font-bold text-slate-800 mb-2">Simulador Vazio</h3>
                  <p className="text-slate-500 text-sm max-w-xs">
                    Você removeu todos os usuários do ambiente de teste local. Cadastre um novo ou clique em <strong className="text-amber-600 font-semibold cursor-pointer hover:underline" onClick={() => fetchUsers(true)}>Resetar Lista</strong> para restaurar os dados iniciais.
                  </p>
                </>
              ) : (
                <>
                  <div className="bg-indigo-50 text-indigo-400 p-4 rounded-full mb-4 shadow-sm">
                    <SearchX size={32} strokeWidth={1.5} />
                  </div>
                  <h3 className="text-lg font-bold text-slate-800 mb-2">Nenhum usuário cadastrado</h3>
                  <p className="text-slate-500 text-sm max-w-xs">
                    A base de dados oficial está vazia no momento. Utilize o formulário ao lado para realizar o primeiro cadastro.
                  </p>
                </>
              )}
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {users.map((user) => (
                <UserCard 
                  key={user.id} 
                  user={user} 
                  onEdit={() => startEditing(user)} 
                  onDelete={() => requestDelete(user.id || null)} 
                />
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
};