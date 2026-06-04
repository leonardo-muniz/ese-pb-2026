export interface User {
  id?: string;
  fullName: string;
  email: string;
}

export interface UserFormData {
  fullName: string;
  email: string;
  password?: string;
}

export interface AlertMessage {
  type: 'success' | 'error' | '';
  text: string;
}

export interface Wallet {
  id: string;
  userId: string;
  currency: string;
  balance: number;
}

export type OrderType = 'BUY' | 'SELL';
export type OrderStatus = 'OPEN' | 'COMPLETED' | 'CANCELLED';

export interface Order {
  id: string;
  userId: string;
  type: OrderType;
  cryptoCurrency: string;
  amount: number;
  price: number;
  status: OrderStatus;
  createdAt: string;
}

export interface CoinMarketData {
  id: string;
  symbol: string;
  name: string;
  currentPrice: number;
  priceChangePercentage24h: number;
}