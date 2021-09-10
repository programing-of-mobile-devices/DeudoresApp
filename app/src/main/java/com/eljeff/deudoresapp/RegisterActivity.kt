package com.eljeff.deudoresapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.deudoresapp.data.local.dao.UserDao
import com.eljeff.deudoresapp.data.local.entities.User
import com.eljeff.deudoresapp.databinding.ActivityRegisterBinding
import java.sql.Types.NULL


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)


        // código del register activity
        registerBinding.registerButton.setOnClickListener {

            // Limpiar errores
            cleanErrors()

            // capturamos datos de la vista
            val email: String = registerBinding.registerEmailEdTx.text.toString()
            val password: String = registerBinding.registerPasswordEdTx.text.toString()
            val confPasswordReg: String = registerBinding.registerRepPasswordEdTx.text.toString()

            // Verificamos que no estén vacios
            val emailEmpty = email.isEmpty()
            val passwEmpty = password.isEmpty()
            val acceptPassw = password.length >= 6

            // bandera de verificación
            var flag_ok = true

            //Si no se ingresa usuario
            with(registerBinding) {

                //Si no se ingresa email
                if (emailEmpty) {
                    registerEmailTxInpLay.error = getString(R.string.input_email_error)
                    flag_ok = false
                }
                //Si no se ingresa cntraseña
                if (passwEmpty) {
                    registerPasswordTxInpLay.error = getString(R.string.input_password_error)
                    flag_ok = false
                }
                //Si no tiene mínimo 6 digitos
                if (!acceptPassw) {
                    registerPasswordTxInpLay.error = getString(R.string.input_len_email_error)
                    flag_ok = false
                }
                // Si se ingresan 6 digitos pero las contraseñas no coinsiden
                if ((password != confPasswordReg) && acceptPassw) {
                    registerRepPasswordTxInpLay.error = getString(R.string.password_not_equal)
                    flag_ok = false
                }

            }

            // despues de verificar campos
            if (flag_ok) {

                createUser(email, password)
                cleanViews()

                Toast.makeText(this, "Usuario creado con exito", Toast.LENGTH_SHORT).show()

                goToLoginActivity()

            }
        }

        // Limpiar campos cada vez que se detecta acción
        with(registerBinding) {

            registerEmailEdTx.setOnClickListener {
                registerEmailTxInpLay.error = null
            }
            registerPasswordEdTx.setOnClickListener {
                registerPasswordTxInpLay.error = null
            }
            registerRepPasswordEdTx.setOnClickListener {
                registerRepPasswordTxInpLay.error = null
            }
        }


    }

    private fun goToLoginActivity() {

        // creamos el intent para ir al loginactivity
        val intent = Intent(this, LoginActivity::class.java)

        // Limpiamos la pila de actividades
        /*intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )*/

        //Se llama la actividad de login
        startActivity(intent)
    }

    private fun createUser(email: String, password: String) {

        // Creamos un objeto tipo user y le asignamos los atributos
        val user = User(id = NULL, email = email, password = password)

        // Intanciamos el userDao en la base de datos de deudores
        val userDao: UserDao = DeudoresApp.database.UserDao()

        // lammamos la clase para crear el usuario
        userDao.createUser(user)

    }

    private fun cleanViews() {

        with(registerBinding) {
            registerEmailEdTx.setText("")
            registerPasswordEdTx.setText("")
            registerRepPasswordEdTx.setText("")
        }

    }

    private fun cleanErrors() {

        with(registerBinding) {

            registerEmailTxInpLay.error = null
            registerPasswordTxInpLay.error = null
            registerRepPasswordTxInpLay.error = null

        }
    }

}