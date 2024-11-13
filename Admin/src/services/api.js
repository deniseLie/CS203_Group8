import axios from 'axios';

const api = axios.create({
  baseURL: 'https://your-api-endpoint.com',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
