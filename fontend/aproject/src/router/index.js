import { createRouter, createWebHistory } from 'vue-router'
import StudentComponent from '@/view/StudentComponent.vue'
import ClassComponent from '@/view/ClassComponent.vue'
import MonHoc from '@/view/MonHoc.vue'
import Login from '@/view/LoginView.vue'

import store from '@/store'
import axios from 'axios'

const routes = [
    {
        path: '/',
        name: 'Student',
        component: StudentComponent,
        meta: {
            authRequired: true
        }
    },
    {
        path: '/class',
        name: 'Class',
        component: ClassComponent,
        meta: {
            authRequired: true
        }
    },
    {
        path: '/monhoc',
        component: MonHoc,
        meta: {
            authRequired: true
        }
    },
    {
        path: '/login',
        name:'login',
        component: Login
    }
]


const router = createRouter({ history : createWebHistory() , routes })

router.beforeEach(async (to,from,next) => {
    if(to.matched.some(record => record.meta.authRequired)){
        if(!store.getters.isLoginIn){
            next({name : 'login'})
            
        } else{
            const response = await axios.post('http://localhost:8080/api/v1/expire',{'token': store.getters.getToken},{
                headers: {
                    'Content-Type': 'application/json'
                }})
            const code = response.data.code
            if(code == 401){
                store.commit('logout')
                next({name : 'login'})
           }else{
                next()
           }  
        }
    } else{
        next()
    }
})

export default router