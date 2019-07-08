import axios from 'axios';

let base = '';

export const requestLogin = params => { return axios.post(`${base}/login`, params).then(res => res.data); };

export const getServicesListPage = params => { return axios.get(`${base}/serv/listpage`, { params: params }); };

export const removeServ = params => { return axios.get(`${base}/serv/remove`, { params: params }); };

export const batchRemoveServ = params => { return axios.get(`${base}/serv/batchremove`, { params: params }); };

export const editServ = params => { return axios.get(`${base}/serv/edit`, { params: params }); };

export const addServ = params => { return axios.get(`${base}/serv/add`, { params: params }); };
