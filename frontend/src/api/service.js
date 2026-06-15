import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default {
  // Clients
  getClients(page = 0, size = 10) {
    return apiClient.get(`/clients?page=${page}&size=${size}`)
  },
  
  getClientById(id) {
    return apiClient.get(`/clients/${id}`)
  },
  
  createClient(clientData) {
    return apiClient.post('/clients', clientData)
  },
  
  updateClient(id, clientData) {
    return apiClient.put(`/clients/${id}`, clientData)
  },
  
  deleteClient(id) {
    return apiClient.delete(`/clients/${id}`)
  },
  
  // Orders
  getOrders(page = 0, size = 10) {
    return apiClient.get(`/orders?page=${page}&size=${size}`)
  },
  
  getOrderById(id) {
    return apiClient.get(`/orders/${id}`)
  },
  
  createOrder(orderData) {
    return apiClient.post('/orders', orderData)
  },
  
  updateOrder(id, orderData) {
    return apiClient.put(`/orders/${id}`, orderData)
  },
  
  getOrdersByClient(clientId, page = 0, size = 10) {
    return apiClient.get(`/orders/client/${clientId}?page=${page}&size=${size}`)
  },
  
  getOrdersByManager(managerId, page = 0, size = 10) {
    return apiClient.get(`/orders/manager/${managerId}?page=${page}&size=${size}`)
  },
  
  getOrdersByMaster(masterId, page = 0, size = 10) {
    return apiClient.get(`/orders/master/${masterId}?page=${page}&size=${size}`)
  },
  
  getOrdersByStatus(status, page = 0, size = 10) {
    return apiClient.get(`/orders/status/${status}?page=${page}&size=${size}`)
  },
  
  updateOrderStatus(id, status) {
    return apiClient.put(`/orders/${id}/status/${status}`)
  },
  
  assignManager(id, managerId) {
    return apiClient.put(`/orders/${id}/assign-manager/${managerId}`)
  },
  
  assignMaster(id, masterId) {
    return apiClient.put(`/orders/${id}/assign-master/${masterId}`)
  },
  
  updateDiagnostics(id, diagnostics) {
    return apiClient.put(`/orders/${id}/diagnostics`, { diagnostics })
  },
  
  approveDiagnostics(id) {
    return apiClient.put(`/orders/${id}/approve`)
  },
  
  rejectRepair(id) {
    return apiClient.put(`/orders/${id}/reject`)
  },
  
  completeRepair(id, repairNotes) {
    return apiClient.put(`/orders/${id}/complete`, { repairNotes })
  },
  
  markAsPaid(id) {
    return apiClient.put(`/orders/${id}/pay`)
  },
  
  returnToClient(id) {
    return apiClient.put(`/orders/${id}/return`)
  },
  
  deleteOrder(id) {
    return apiClient.delete(`/orders/${id}`)
  }
}
