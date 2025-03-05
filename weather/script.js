async function getweather() {
  const API_key = ''; 
  const cities = ['tokyo','osaka','fukuoka','sapporo'];
  const cityNames = {
      'tokyo' : '東京',
      'osaka' : '大阪',
    'fukuoka' : '福岡',
    'sapporo' : '札幌',
  };

  const weatherContainer = document.getElementById('weather');

  weatherContainer.innerHTML = '';
  
  for (const city of cities) {
    const URL = `https://api.openweathermap.org/data/2.5/weather?q=${city}&lang=ja&units=metric&appid=${API_key}`;
    
    try {
      const response = await fetch(URL);
      if (!response.ok) {
        throw new Error(`HTTPエラー!ステータス: ${response.status}`);
      }
      const data = await response.json();
      
      const weather = data.weather[0].description;
      const icon = `https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png`;
      const temperature = data.main.temp;
      const windspeed = data.wind.speed;

      const article = document.createElement('article');
      article.classList.add('p-2', 'border', 'border-success');

      const cityName = cityNames[city] || city;
      
      article.innerHTML = `
      <h4 class="mb-3 text-decoration-underline">${cityName}の天気</h4>
      <section class="weather-data">
        <div>天気</div><div>${weather}</div>
        <div>アイコン</div><div><img src="${icon}" alt="天気アイコン"></div>
        <div>気温</div><div>${temperature}℃</div>
        <div>風速</div><div>${windspeed}m/s</div>
      </section>
      `;
      
      weatherContainer.appendChild(article);
    
    } catch(error) {
      console.error('エラー:', error);
    }
  }
}

window.onload = getweather;
