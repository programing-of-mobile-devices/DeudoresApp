package com.eljeff.deudoresapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.deudoresapp.data.local.dao.UserDao
import com.eljeff.deudoresapp.data.local.entities.User
import com.eljeff.deudoresapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        // código del login

        // Cuando se llama el boton de ingresar
        loginBinding.loginButton.setOnClickListener {

            //Limpiar errores
            cleanErrors()

            // Capturamos datos de la vista
            val email = loginBinding.loginEmailEdTx.text.toString()
            val password = loginBinding.loginPasswordEdTx.text.toString()

            //Validación de campos
            val emailEmpty = email.isEmpty()
            val passwEmpty = password.isEmpty()
            var validate = true

            // validamos si estaan vacios
            with(loginBinding) {

                if (emailEmpty) {
                    loginEmailTxInpLay.error = getString(R.string.input_email_error)
                    validate = false
                }
                if (passwEmpty) {
                    loginPasswordTxInpLay.error = getString(R.string.input_password_error)
                    validate = false
                }

            }

            // si no estan vacios verificamos la existencia y igualdad en las contraseñas
            if (validate) {

                val flagLoginOk = searchUsers(email, password)

                if(flagLoginOk){
                    // limpiamos los campos
                    cleanViews()
                    // vamos al main activiti
                    goToMainActivity()
                }
            }
        }

        // Cuando se llama el edit text para Registrarse
        loginBinding.loginRegisterTxVw.setOnClickListener {

            goToRegisterActivity()

        }

        // Limpiar campos cada vez que se detecta acción
        with(loginBinding) {

            loginEmailEdTx.setOnClickListener {
                loginEmailTxInpLay.error = null
            }
            loginPasswordEdTx.setOnClickListener {
                loginPasswordTxInpLay.error = null
            }

        }


    }

    private fun goToRegisterActivity() {

        // Contenedor para tranferir datos entre actividades
        val intent = Intent(this, RegisterActivity::class.java)

        //Iniciamos Register activity
        startActivity(intent)
    }

    private fun cleanViews() {
        with(loginBinding) {
            loginEmailEdTx.setText("")
            loginPasswordEdTx.setText("")
        }
    }

    private fun goToMainActivity() {

        // creamos el intent para ir al mainnactivity
        val intent = Intent(this, MainActivity::class.java)

        // Limpiamos la pila de actividades
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        //Se llama la actividad de main
        startActivity(intent)
    }

    private fun searchUsers(email: String, password: String): Boolean {

        // instanciamos base de datos del usuario
        val userDao: UserDao = DeudoresApp.database.UserDao()
        // función que busca el usuario y lo retorna
        val user: User = userDao.searchUser(email)
        var flagOut = false

        // si el usuario existe
        if (user != null) {

            // verificamos igualdad en las contraseñas
            if (password == user.password) {

                // alles gut
                flagOut = true

            } else {
                // error en las contraseñas
                loginBinding.loginPasswordTxInpLay.error = getString(R.string.password_not_equal)
                flagOut = false
            }


        } else {

            // error en los correos
            loginBinding.loginEmailTxInpLay.error = getString(R.string.non_existent_user)
            Toast.makeText(this, "Por favor registrese.", Toast.LENGTH_SHORT).show()

            flagOut = false
        }

        return flagOut
    }


    private fun cleanErrors() {
        with(loginBinding) {
            loginEmailTxInpLay.error = null
            loginPasswordTxInpLay.error = null
        }
    }
}