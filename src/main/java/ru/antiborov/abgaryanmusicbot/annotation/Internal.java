package ru.antiborov.abgaryanmusicbot.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation indicates that what it is set upon is used internally and <b>not recommended</b> to be used
 * by the end users
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Internal {
}
