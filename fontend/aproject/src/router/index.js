import { createRouter, createWebHistory } from "vue-router";
import StudentComponent from "@/view/StudentComponent.vue";
import ClassComponent from "@/view/ClassComponent.vue";
import MonHoc from "@/view/MonHoc.vue";
import Login from "@/view/LoginView.vue";
import Register from "@/view/RegisterComponent.vue";
import Unauthorize from "@/view/UnauthorizeComponent.vue";
import Detail from '@/view/DetailComponent.vue'
import StudentDetail from '@/view/DetailStudent.vue'
import Student from '@/view/UserHomePage.vue'
import StdClass from '@/view/UserClassPage.vue'

import store from "@/store";
import axios from "axios";

const routes = [
  {
    path: "/",
    name: "User",
    component: StudentComponent,
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_SUPERVISOR"
      ]
    },
  },
  {
    path: "/class",
    name: "Class",
    component: ClassComponent,
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_SUPERVISOR"
      ]
    },
  },
  {
    path: "/monhoc",
    component: MonHoc,
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_SUPERVISOR"
      ]
    },
  },
  {
    path: "/login",
    name: "login",
    component: Login,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/detail",
    name: "Detail", 
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_SUPERVISOR"
      ]
    },
    component: Detail,
  },
  {
    path: "/student/",
    name: "Student", 
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_USER"
      ]
    },
    component: Student,
  },
  {
    path: "/student/class",
    name: "StdClass", 
    meta: {
      authRequired: true,
      role: [
        "ROLE_ADMIN",
        "ROLE_USER"
      ]
    },
    component: StdClass,
  },
  {
    path: "/detail/student/:id",
    name: "student", 
    meta: {
      authRequired: true,
      role: [
        "ROLE_USER",
        "ROLE_ADMIN"
      ]
    },
    props: {
      id: ''
    },
    component: StudentDetail,
  },
  {
    path: "/unauthorize",
    name: "unauthorize",
    component: Unauthorize,
  },
];

const router = createRouter({ history: createWebHistory(), routes });

router.beforeEach(async (to, from, next) => {
  if (to.matched.some((record) => record.meta.authRequired)) {
    if (!store.getters.isLoginIn) {
      next({ name: "login" });
    } else {
      const response = await axios.post(
        "http://localhost:8080/api/v1/expire",
        { token: store.getters.getToken },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const code = response.data.code;
      if (code == 401) {
        store.commit("logout");
        next({ name: "login" });
      } else if (!to.meta.role.includes(store.getters.getAuthorities)) {
        store.commit('confirmAuthor',false)
        next({ name: "unauthorize" });
      } else {
        store.commit('confirmAuthor',true)
        next();
      }
    }
  } else {
    next();
  }
});

export default router;
