package com.example.a10db

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.a10db.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var ream: Ream

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Shared Preferences
        binding.buttonForAddSP.setOnClickListener {

            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(getString(R.string.name_pref),
                    binding.editName.text.toString())
                putBoolean(getString(R.string.gender_pref),
                    binding.switchManWoman.isChecked)
                putInt(getString(R.string.age_pref),
                    binding.editAge.text.toString().toInt())
                apply()

                Toast.makeText(
                    applicationContext,
                    "Add information about you to BD",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.buttonForGoToSP.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.preference_file_key),
                MODE_PRIVATE) ?: return@setOnClickListener

            val nameFromBD = sharedPref.getString(getString(R.string.name_pref), "name")
            val genderFromBD = sharedPref.getBoolean(getString(R.string.gender_pref), false)
            val ageFromBD = sharedPref.getInt(getString(R.string.age_pref), 0)

            //val txt = binding.viewBD.text

            var genderString = ""

            if (genderFromBD == true){
                genderString = "woman"
            }else{
                genderString = "man"
            }

            binding.viewBD.text = "${nameFromBD} \t gender: ${genderString} \t age: ${ageFromBD} \n"
        }

        val db = Room.databaseBuilder(applicationContext,
            UserDB::class.java, "database-name"
        ).build()

        val userDao = db.userDao()
        val name = binding.editName.text.toString()
        val age = binding.editAge.text.toString()


        binding.buttonForAddRM.setOnClickListener {
            if(name.isNotEmpty() && age.isNotEmpty()) {

                val user = User(name = name, gender = binding.switchManWoman.isChecked , age = age.toInt())

                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.insertAll(user)
                }

                Toast.makeText(
                    applicationContext,
                    "Personal data saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonForGoToRM.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                val users = userDao.getAll()
                var usersInfo = ""
                var gs  = ""

                users.forEach {
                    if(it.gender == true){
                        gs = "woman"
                    }else
                        gs = "man"
                    usersInfo += "Name: ${it.name} Gender: ${gs} Age: ${it.age}\n"
                }

                runOnUiThread {
                    binding.viewBD.text = usersInfo
                }
            }

            Toast.makeText(
                applicationContext,
                "Data from the DB has been read",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}