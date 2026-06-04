import { useState, useEffect, useCallback } from 'react';
import type { CoinMarketData } from '../types';

export interface ChartDataPoint {
  time: string;
  price: number;
}

export function useCoinGecko(coinId: string = 'bitcoin') {
  const [coinData, setCoinData] = useState<CoinMarketData | null>(null);
  const [chartData, setChartData] = useState<ChartDataPoint[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<boolean>(false);

  const fetchCoinData = useCallback(async () => {
    try {
      // Busca o preço atual
      const priceRes = await fetch(
        `https://api.coingecko.com/api/v3/simple/price?ids=${coinId}&vs_currencies=usd&include_24hr_change=true`
      );
      if (!priceRes.ok) throw new Error('Falha na API CoinGecko Price');
      const priceData = await priceRes.json();

      // Busca os dados históricos das últimas 24h para o gráfico
      const chartRes = await fetch(
        `https://api.coingecko.com/api/v3/coins/${coinId}/market_chart?vs_currency=usd&days=1`
      );
      if (!chartRes.ok) throw new Error('Falha na API CoinGecko Chart');
      const chartRawData = await chartRes.json();

      // Formata os dados do gráfico convertendo timestamp para hora:minuto
      const formattedChartData = chartRawData.prices.map((item: [number, number]) => {
        const date = new Date(item[0]);
        return {
          time: `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`,
          price: item[1]
        };
      });

      setCoinData({
        id: coinId,
        symbol: coinId === 'bitcoin' ? 'BTC' : coinId.toUpperCase(),
        name: coinId.charAt(0).toUpperCase() + coinId.slice(1),
        currentPrice: priceData[coinId].usd,
        priceChangePercentage24h: priceData[coinId].usd_24h_change
      });
      
      // Amostragem: Pega um ponto a cada 30 minutos (índice múltiplo de 6) para o gráfico não ficar poluído
      setChartData(formattedChartData.filter((_: unknown, i: number) => i % 6 === 0));
      setError(false);
    } catch (err) {
      setError(true);
    } finally {
      setLoading(false);
    }
  }, [coinId]);

  useEffect(() => {
    fetchCoinData();
    const interval = setInterval(fetchCoinData, 60000); // Atualiza a cada 1 minuto
    return () => clearInterval(interval);
  }, [fetchCoinData]);

  return { coinData, chartData, loading, error, refreshData: fetchCoinData };
}