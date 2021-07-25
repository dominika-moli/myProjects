import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import CertificationDetail from "../views/CertificationDetail.vue";
import Profile from "../views/Profile.vue";
import Register from "../views/Register.vue";
import ResetPassword from "../views/ResetPassword.vue";
import ChangePassword from "../views/ChangePassword.vue";
import VoucherDetail from "../views/VoucherDetail.vue";
import UserDetail from "../views/UserDetail.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },

  {
    path: "/register",
    name: "Register",
    component: Register,
  },

  {
    path: "/resetPassword",
    name: "ResetPassword",
    component: ResetPassword,
  },
  {
    path: "/resetPassword/:token",
    name: "ChangePassword",
    component: ChangePassword,
  },

  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/certificationDetail/:id",
    name: "CertificationDetail",
    component: CertificationDetail,
  },
  {
    path: "/voucherDetail/:id",
    name: "VoucherDetail",
    component: VoucherDetail
  },
  {
    path: "/userDetail/:id",
    name: "UserDetail",
    component: UserDetail
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
