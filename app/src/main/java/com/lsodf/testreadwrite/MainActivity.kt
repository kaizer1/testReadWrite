package com.lsodf.testreadwrite

import android.app.AlertDialog
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import java.io.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {


    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null
    private var enableNotificationListenerAlertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         findViewById<Button?>(R.id.wrib).setOnClickListener {
            println(" press writeData ")
             writeMyFiles()
        }

        findViewById<Button?>(R.id.read).setOnClickListener {
            println(" press readData ")
            readMyFiles()
        }


        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.lsodf.testreadwrite.notificationlistenerexample")
        registerReceiver(imageChangeBroadcastReceiver, intentFilter)

                if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog!!.show()
        }


    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(imageChangeBroadcastReceiver)
    }

    private fun readMyFiles() {


        val `is`: FileInputStream
        val reader: BufferedReader

        val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "losWriteTestApps")
        val file = File(f,
            "loaded11.txt"
        )


        var values: Int = 0
        if (file.exists()) {
            `is` = FileInputStream(file)
            reader = BufferedReader(InputStreamReader(`is`))
            var line = reader.readLine()
            while (line != null) {
               // Log.d("! ", line )
                line = reader.readLine()

                       if(values == 4){
                       // Log.d("df", " empty read ")

                       // db.insertUserDetails(title,text,packages, times)
                        title = ""
                        //text = ""
                        //packages = ""
                        //times = ""
                        values = 0
                    }else{

                        when(values){
                            0 ->   println("Title $line") //title = line
                            1 ->   println("Text $line ")//text = line
                            2 ->   println("Package $line")//packages = line
                            3 ->   println("Time $line")//times = line
                        }
                        values++
                    }


            }


        }

    }

    class ImageChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotificationCode = intent.getIntExtra("Notification Code", -1)
            //  changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }

    override fun onResume() {
        super.onResume()



        println(" my Call sadfasdfsd ")
    }


    private fun buildNotificationServiceAlertDialog(): AlertDialog? {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes,
            DialogInterface.OnClickListener { dialog, id ->
                startActivity(
                    Intent(
                        ACTION_NOTIFICATION_LISTENER_SETTINGS
                    )
                )

                //  startActivityForResult(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUEST_CODE_PERMISSIONS_NOTIFY)

                //resultLauncher.launch(intent)

            })
        alertDialogBuilder.setNegativeButton(R.string.no,
            DialogInterface.OnClickListener { dialog, id ->

            })
        return alertDialogBuilder.create()
    }

    private fun writeMyFiles(){


//        val path : File? = application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//        val foldermain  = "losWriteTestApps"
//
//
//        println(" my path = $path")
//        val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), foldermain)
//
//        if(!f.exists()){
//            f.mkdir()
//        }
//
//        println( " my f = ${f.toString()}")
//        val file = File(f, "loaded11.txt")
//
//        try {
//            //
//            val outputStreamWriter  = OutputStreamWriter(file.outputStream(), Charset.defaultCharset())
//
//            //val bufferWri = BufferedWriter(file.outputStream(), Charset.defaultCharset())
//            // for (item in arraylist){
//
//            val item = "Title my messages"
//            outputStreamWriter.write("tit" + "\n")
//            outputStreamWriter.write("dfwf" + "\n")
//            outputStreamWriter.write(" sdf" + "\n")
//            outputStreamWriter.write("DSF" + "\n")
//            // }
//
//            outputStreamWriter.close();
//        }
//        catch (e : IOException) {
//            println("Exception File write failed:  ${e.toString()} ");
//        }





        val fos: FileOutputStream

        try {
            val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "losWriteTestApps")
            fos = FileOutputStream( File (f , "loaded11.txt")  ,  true)
            val fWriter: FileWriter
            try {
                fWriter = FileWriter(fos.fd)
                fWriter.write("newValues" + "\n")
                fWriter.write("23" + "\n")
                fWriter.write("packaName23" + "\n")
                fWriter.write("LKSDLKSd838383883" + "\n\n")
                fWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fos.fd.sync()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


}