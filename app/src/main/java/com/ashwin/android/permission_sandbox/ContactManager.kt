package com.ashwin.android.permission_sandbox

import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.provider.ContactsContract
import android.util.Log

private const val NO_CONTACT_FOUND = -1L

private fun getContactId(context: Context, name: String, email: String): Long {
    val uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI  //ContactsContract.Data.CONTENT_URI
    val projection = null  // or arrayOf(ContactsContract.Contacts.DISPLAY_NAME, Phone.NUMBER)
    val cursor: Cursor? = context.contentResolver.query(uri, projection, "${ContactsContract.PhoneLookup.DISPLAY_NAME} = ? AND ${ContactsContract.CommonDataKinds.Email.DATA} = ?", arrayOf(name, email), null)
    cursor?.use {
        Log.d("contact-app", "email-cursor count: ${it.count}")
        if (it.count > 0) {
            it.moveToFirst()
            val emailData = DatabaseUtils.dumpCursorToString(it)
            Log.d("contact-app", "email-cursor data: $emailData")
            return it.getLong(it.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID))
        }
    }
    return NO_CONTACT_FOUND
}

internal fun getContact(context: Context, name: String, email: String): Map<String, Any?>? {
    val id = getContactId(context, name, email)
    if (id != NO_CONTACT_FOUND) {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.Data.RAW_CONTACT_ID} = ?",
            arrayOf(id.toString()),
            null
        )
        cursor?.use { it ->
            Log.d("contact-app", "phone-cursor count: ${it.count}")

            val phones = mutableListOf<String>()
            if (it.count > 0) {
                it.moveToFirst()
                val phoneData = DatabaseUtils.dumpCursorToString(it)
                Log.d("contact-app", "phone-cursor data: $phoneData")

                do {
                    val p = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    phones.add(p)
                } while (it.moveToNext())
            }

            val contact = mapOf<String, Any?>(
                Pair("id", id),
                Pair("name", name),
                Pair("email", email),
                Pair("phones", phones)
            )
            Log.d("contact-app", "getContact: $contact")
            return contact
        }
    }
    return null
}
