import axios from 'axios';

const api = axios.create({
  baseURL: 'http://CS203-ALB-1399477535.ap-southeast-1.elb.amazonaws.com:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
