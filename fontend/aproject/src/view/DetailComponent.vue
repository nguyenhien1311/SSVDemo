<template>
    <div style="width: 1000px;">
        <br />
        <h3>Account details</h3>
        <table class="table table-bordered table-striped table-hover">
            <tbody>
                <tr>
                    <th scope="row"></th>
                    <td>
                        <input type="hidden" v-model="id">
                    </td>
                </tr>
                <tr>
                    <th scope="row"><label>Full name</label></th>
                    <td>
                        <input ref="name" class="form-control col-sm-8" type="text" v-model="state.fullName" />
                        <span v-if="v$.fullName.$error" style="color: red;">{{ v$.fullName.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><label>Email Address</label></th>
                    <td>
                        <input ref="email" class="form-control col-sm-8" type="text" v-model="state.email" />
                        <span v-if="v$.email.$error" style="color: red;">{{ v$.email.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Phone number</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8" type="text" v-model="state.phone" />
                        <span v-if="v$.phone.$error" style="color: red;">{{ v$.phone.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Username</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8" type="text" v-model="state.username" />
                        <span v-if="v$.username.$error" style="color: red;">{{ v$.username.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Password</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8"
                            placeholder="Leave this field blank if not want to change password" type="text"
                            v-model="state.password" />
                        <span v-if="v$.password.$error" style="color: red;">{{ v$.password.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"></th>
                    <td colspan="2">
                        <input class="btn btn-primary " type="submit" @click="saveUser()" value="Save" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required, email, minLength, maxLength, helpers } from '@vuelidate/validators'
import axios from 'axios'

export default {
    setup() {
        const state = reactive({
            fullName: '',
            username: '',
            password: '',
            email: '',
            phone: '',
        })

        const onlyDigits = /^[a-zA-Z ]*$/;
        //  const phoneNumbers = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;

        const isNameValid = (value) => onlyDigits.test(value)
        // const isPhoneValid = (value) => phoneNumbers.test(value)

        const rules = computed(() => {
            return {
                fullName: {
                    required,
                    minLength: minLength(5),
                    maxLength: maxLength(40),
                    isNameValid: helpers.withMessage('Name must be only digits', isNameValid)
                },
                email: { required, email },
                phone: {
                    required,
                    minLength: minLength(10),
                    maxLength: maxLength(10)
                    // isPhoneValid: helpers.withMessage('Phone number not valid ! \n-start with 84|0[3|5|7|8|9]',isPhoneValid)
                },
                username: { required },
                password: { minLength: minLength(5) }
            }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    data() {
        return {
            id: '',
        }
    },
    created() {
        this.loadDetail()
    },
    methods: {
        async loadDetail() {
            this.id = this.$store.state.userId
            const response = await axios.get('http://localhost:8080/api/v1/user/' + this.id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = response.data.body

            this.loadData(data)

        },
        loadData(data) {
            this.state.fullName = data.fullName
            this.state.email = data.email
            this.state.phone = data.phone
            this.state.username = data.username
        },
        saveUser() {
            this.v$.$validate();
            if (!this.v$.$error) {
                const data = {
                    'name': this.state.fullName,
                    'email': this.state.email,
                    'phone': this.state.phone,
                    'username': this.state.username,
                    'password': this.state.password
                }
                axios.put('http://localhost:8080/api/v1/user/' + this.id, data, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                }).then(response => (this.handleData(response.data)))
            }
        },
        handleData(data) {  
            console.log(data)
            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }
            
            // window.location.reload()
        },
    }
}
</script>

<style>

</style>