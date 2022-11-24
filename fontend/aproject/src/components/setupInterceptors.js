import axiosInstance from './api'

const setup = (store) =>{
    axiosInstance.interceptors.request.use(
        (config) => {
            const token = store.getters.getToken;
            if(token){
                config.headers["Authorization"] = 'Bearer ' + token;
            }
            return config;
        },
        (error) => {
            console.log(error)
            return Promise.reject(error);
        }
    );

    axiosInstance.interceptors.response.use(
        (res) => {
            return res;
        },
        async (err) => {
            console.log(err)
            const originalConfig = err.config;

        if (originalConfig.url !== "/refresh" && err.response) {
            // Access Token was expired
            if (err.response.code === 401 && !originalConfig._retry) {
            originalConfig._retry = true;

            try {
                const rs = await axiosInstance.post("/refreshtoken", {
                refreshToken: store.getters.getRefreshToken,
                });

                const { accessToken } = rs.data.body.token.refreshToken;
                console.log("Refresh new token : "+accessToken)
                store.dispatch('/refreshToken', accessToken);
                store.commit('refreshToken',accessToken);

                return axiosInstance(originalConfig);
            } catch (_error) {
                return Promise.reject(_error);
            }
         }
      }

      return Promise.reject(err);
    }
    );
};

export default setup;