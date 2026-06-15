import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import ClientsList from '../views/ClientsList.vue'
import OrdersList from '../views/OrdersList.vue'
import OrderDetails from '../views/OrderDetails.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/clients',
    name: 'ClientsList',
    component: ClientsList
  },
  {
    path: '/orders',
    name: 'OrdersList',
    component: OrdersList
  },
  {
    path: '/orders/:id',
    name: 'OrderDetails',
    component: OrderDetails
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
