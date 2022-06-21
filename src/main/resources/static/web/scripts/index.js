Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            firstNameSingUp: "",
            lastNameSingUp: "",
            emailSingUp: "",
            passwordSingUp: "",
            error1: false,
            error2: false,
            messageError:"",


        }
    },

    created() {

    },

    methods: {

        login() {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => window.location.href = "../web/accounts.html")
                .catch(error => {
                    if (error.response.status == 401) {
                        this.error1 = true
                    }

                });

        },

        loginSingUp() {
            axios.post('/api/clients', `firstName=${this.firstNameSingUp}&lastName=${this.lastNameSingUp}&email=${this.emailSingUp}&password=${this.passwordSingUp}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    this.email = this.emailSingUp,
                        this.password = this.passwordSingUp,
                        this.login()
                }
                )
                .catch(error =>{
                    if (error.response.status == 403){
                        this.error2 = true
                        this.messageError = error.response.data
                    }

                }

                )
        }

    }


}).mount('#app')