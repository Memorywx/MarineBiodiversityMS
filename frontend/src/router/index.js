import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/RegisterView.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    component: () => import('../views/LayoutView.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/DashboardView.vue') },
      { path: 'species', name: 'Species', component: () => import('../views/SpeciesView.vue') },
      { path: 'species/:id', name: 'SpeciesDetail', component: () => import('../views/SpeciesDetailView.vue') },
      { path: 'ecosystem', name: 'Ecosystem', component: () => import('../views/EcosystemView.vue') },
      { path: 'observation', name: 'Observation', component: () => import('../views/ObservationView.vue') },
      { path: 'user', name: 'User', component: () => import('../views/UserView.vue') },
      { path: 'operation-logs', name: 'OperationLogs', component: () => import('../views/OperationLogView.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../views/ProfileView.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (!to.meta.public && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
