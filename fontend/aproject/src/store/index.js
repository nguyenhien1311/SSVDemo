
import {createStore} from 'vuex';
import createaPersistedState from 'vuex-persistedstate'

export default createStore({
    state:  {
            token: '',
            userId:'',
            userName: '',
            isLogin : false,
    },
    plugins:[createaPersistedState({
        paths: ['token','userId','userName','isLogin']
    })],
    mutations:{
        setUser(state, newUser){
            state.token = newUser.token
            state.userId = newUser.userId
            state.userName = newUser.userName
            state.isLogin = true
        },
        logout(state){
            state.token = ''
            state.userId = ''
            state.userName = ''
            state.isLogin = false
        }
    },
    getters:{
        isLoginIn : state => state.isLogin,
        getToken : state => state.token
    }
})



