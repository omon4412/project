import {createRouter, createWebHistory} from "vue-router";
import LoginPage from "@/pages/AuthArea/LoginPage.vue";
import MainPage from "@/pages/MainPage.vue";
import NotFound from "@/pages/NotFound.vue";
import RegisterPage from "@/pages/AuthArea/RegisterPage.vue";
import LogoutPage from "@/pages/AuthArea/LogoutPage.vue";
import ProfilePage from "@/pages/AuthArea/ProfilePage.vue";
import AdminPage from "@/pages/AuthArea/AdminPage.vue";
import ForbiddenPage from "@/pages/ForbiddenPage.vue";
import UsersPage from "@/pages/UsersPage.vue";

const routes = [
    {
        path: '/',
        component: MainPage
    },
    {
        path: '/login',
        component: LoginPage
    },
    {
        path: '/register',
        component: RegisterPage
    },
    {
        path: '/logout',
        component: LogoutPage
    },
    {
        path: '/profile',
        component: ProfilePage
    },
    {
        path: '/admin',
        component: AdminPage,
        children: [
            {
                path: 'users',
                component: UsersPage,
            },
            {
                path: 'users/:userId',
                component: ProfilePage
            },
            {
                path: 'test2',
                component: ForbiddenPage,
            },
        ]
    },
    {
        path: '/404',
        component: NotFound
    },
    {
        path: '/403',
        component: ForbiddenPage
    },
    {
        path: "/:catchAll(.*)",
        redirect: '/404'
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: routes
});

export default router;