
import {createStore} from 'vuex';
import createaPersistedState from 'vuex-persistedstate'

export default createStore({
    state:  {
            token: '',
            userId:'',
            userName: '',
            authorities: '',
            isLogin : false,
            isAuthorited: true
    },
    plugins:[createaPersistedState({
        paths: ['token','userId','userName','authorities','isLogin','isAuthorited']
    })],
    mutations:{
        setUser(state, newUser){
            state.token = newUser.token
            state.userId = newUser.userId
            state.userName = newUser.userName
            state.authorities = newUser.authorities
            state.isLogin = true
        },
        logout(state){
            state.token = ''
            state.userId = ''
            state.userName = ''
            state.authorities = ''
            state.isLogin = false
            state.isAuthorited= false
        },
        confirmAuthor(state,value){
            state.isAuthorited = value;
        }
    },
    getters:{
        isLoginIn : state => state.isLogin,
        getToken : state => state.token,
        getAuthorities : state =>  state.authorities,
        isAuthorited : state => state.isAuthorited
    }
})



