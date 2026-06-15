<template>
  <div class="dashboard">
    <h1 class="mb-4">
      <i class="bi bi-speedometer2"></i> Панель управления
    </h1>

    <div class="row">
      <div class="col-md-3 mb-4">
        <div class="card bg-primary text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title mb-0">Всего заказов</h6>
                <h3 class="mb-0">{{ stats.totalOrders }}</h3>
              </div>
              <i class="bi bi-list-check" style="font-size: 2rem;"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-4">
        <div class="card bg-success text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title mb-0">Завершено</h6>
                <h3 class="mb-0">{{ stats.completedOrders }}</h3>
              </div>
              <i class="bi bi-check-circle" style="font-size: 2rem;"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-4">
        <div class="card bg-warning text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title mb-0">В работе</h6>
                <h3 class="mb-0">{{ stats.inProgressOrders }}</h3>
              </div>
              <i class="bi bi-hourglass-split" style="font-size: 2rem;"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-4">
        <div class="card bg-info text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title mb-0">Клиентов</h6>
                <h3 class="mb-0">{{ stats.totalClients }}</h3>
              </div>
              <i class="bi bi-people" style="font-size: 2rem;"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row mt-4">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header bg-dark text-white">
            <h5 class="mb-0">Недавние заказы</h5>
          </div>
          <div class="card-body">
            <div v-if="recentOrders.length" class="table-responsive">
              <table class="table table-sm">
                <thead>
                  <tr>
                    <th>№ Заказа</th>
                    <th>Клиент</th>
                    <th>Статус</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="order in recentOrders" :key="order.id">
                    <td><router-link :to="`/orders/${order.id}`">{{ order.orderNumber }}</router-link></td>
                    <td>{{ order.clientName }}</td>
                    <td>
                      <span :class="`badge bg-${getStatusColor(order.status)}`">
                        {{ getStatusText(order.status) }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else class="alert alert-info">Нет заказов</div>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card">
          <div class="card-header bg-dark text-white">
            <h5 class="mb-0">Статистика по статусам</h5>
          </div>
          <div class="card-body">
            <div v-for="(count, status) in statusStats" :key="status" class="mb-3">
              <div class="d-flex justify-content-between mb-2">
                <span>{{ getStatusText(status) }}</span>
                <span class="badge bg-secondary">{{ count }}</span>
              </div>
              <div class="progress">
                <div class="progress-bar" :style="{ width: (count / stats.totalOrders * 100) + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../api/service'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalOrders: 0,
        completedOrders: 0,
        inProgressOrders: 0,
        totalClients: 0
      },
      recentOrders: [],
      statusStats: {}
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const ordersResponse = await apiService.getOrders(0, 100)
        this.stats.totalOrders = ordersResponse.data.totalElements || 0
        this.stats.completedOrders = ordersResponse.data.content.filter(o => o.status === 'RETURNED').length
        this.stats.inProgressOrders = ordersResponse.data.content.filter(o => o.status === 'IN_PROGRESS').length
        this.recentOrders = ordersResponse.data.content.slice(0, 5)
        
        this.calculateStatusStats(ordersResponse.data.content)
      } catch (error) {
        console.error('Error loading stats:', error)
      }
    },
    calculateStatusStats(orders) {
      const stats = {}
      orders.forEach(order => {
        stats[order.status] = (stats[order.status] || 0) + 1
      })
      this.statusStats = stats
    },
    getStatusText(status) {
      const statusMap = {
        'CREATED': 'Создана',
        'RECEIVED': 'Принята',
        'DIAGNOSTICS': 'Диагностика',
        'AWAITING_APPROVAL': 'Ожидание',
        'APPROVED': 'Одобрена',
        'REJECTED': 'Отклонена',
        'IN_PROGRESS': 'В работе',
        'COMPLETED': 'Завершена',
        'PAID': 'Оплачена',
        'RETURNED': 'Выдана'
      }
      return statusMap[status] || status
    },
    getStatusColor(status) {
      const colorMap = {
        'CREATED': 'secondary',
        'RECEIVED': 'info',
        'DIAGNOSTICS': 'warning',
        'AWAITING_APPROVAL': 'warning',
        'APPROVED': 'info',
        'REJECTED': 'danger',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success',
        'PAID': 'success',
        'RETURNED': 'success'
      }
      return colorMap[status] || 'secondary'
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.card {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
}

.card-header {
  border-bottom: 1px solid #dee2e6;
}

.bi {
  margin-right: 0.5rem;
}
</style>
