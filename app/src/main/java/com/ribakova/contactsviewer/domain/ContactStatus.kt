package com.ribakova.contactsviewer.domain

import com.ribakova.contactsviewer.R

enum class ContactStatus(val labelId: Int) {
    MOTHER(R.string.contact_status_mother),
    FATHER(R.string.contact_status_father),
    BROTHER(R.string.contact_status_brother),
    SISTER(R.string.contact_status_sister),
    GRANDFATHER(R.string.contact_status_grandfather),
    GRANDMOTHER(R.string.contact_status_grandmother),
    FRIEND(R.string.contact_status_friend),
    GIRLFRIEND(R.string.contact_status_girlfriend),
    DOCTOR(R.string.contact_status_doctor),
    CLASSMATE(R.string.contact_status_classmate),
    ROOMMATE(R.string.contact_status_roommate),
    BESTFRIEND(R.string.contact_status_bestfriend),
    HUSBAND(R.string.contact_status_husband),
    WIFE(R.string.contact_status_wife),
    AUNT(R.string.contact_status_aunt)
}