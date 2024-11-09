import axios from 'axios';

const api = axios.create({
  baseURL: 'https://your-api-endpoint.com',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const fetchUsers = () => api.get('/users');
export const fetchReports = () => api.get('/reports');
export default api;
