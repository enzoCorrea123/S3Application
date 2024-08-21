import axios from 'axios';

const api = axios.create({
  baseURL: 'http://demo:8089/',
});

export default api;