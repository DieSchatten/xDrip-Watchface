package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;


import com.klaus3d3.xDripwatchface.Constants;
import com.klaus3d3.xDripwatchface.data.Battery;
import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.data.HeartRate;
import com.klaus3d3.xDripwatchface.data.Steps;
import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptNumView;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.digital.SlptDayHView;
import com.ingenic.iwds.slpt.view.digital.SlptDayLView;
import com.ingenic.iwds.slpt.view.digital.SlptHourHView;
import com.ingenic.iwds.slpt.view.digital.SlptHourLView;
import com.ingenic.iwds.slpt.view.digital.SlptMinuteHView;
import com.ingenic.iwds.slpt.view.digital.SlptMinuteLView;
import com.ingenic.iwds.slpt.view.digital.SlptMonthHView;
import com.ingenic.iwds.slpt.view.digital.SlptMonthLView;
import com.ingenic.iwds.slpt.view.digital.SlptWeekView;
import com.ingenic.iwds.slpt.view.digital.SlptYear0View;
import com.ingenic.iwds.slpt.view.digital.SlptYear1View;
import com.ingenic.iwds.slpt.view.digital.SlptYear2View;
import com.ingenic.iwds.slpt.view.digital.SlptYear3View;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.resource.ResourceManager;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;


public class MainClock extends DigitalClockWidget {

    private TextPaint hourFont;
    private TextPaint minutesFont;
    private TextPaint secondsFont;
    private TextPaint indicatorFont;
    private TextPaint dateFont;
    private TextPaint dayFont;
    private TextPaint weekdayFont;
    private TextPaint monthFont;
    private TextPaint yearFont;

    private boolean secondsBool;
    private boolean indicatorBool;
    private boolean indicatorFlashBool;
    private boolean dateBool;
    private boolean weekdayBool;
    private boolean dayBool;
    private boolean monthBool;
    private boolean month_as_textBool;
    private boolean three_letters_month_if_textBool;
    private boolean yearBool;
    private boolean three_letters_day_if_textBool;
    private boolean dateAlignLeftBool;
    private boolean weekdayAlignLeftBool;
    private boolean dayAlignLeftBool;
    private boolean monthAlignLeftBool;
    private boolean yearAlignLeftBool;
    private boolean no_0_on_hour_first_digit;

    private Drawable background;
    private float leftHour;
    private float topHour;
    private float leftMinute;
    private float topMinute;
    private float leftSeconds;
    private float topSeconds;
    private float leftIndicator;
    private float topIndicator;
    private float leftDate;
    private float topDate;
    private float leftDay;
    private float topDay;
    private float leftMonth;
    private float topMonth;
    private float leftWeekday;
    private float topWeekday;
    private float leftYear;
    private float topYear;

    private String[] digitalNums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] digitalNumsNo0 = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//no 0 on first digit

    public static String[] colors = {"#ff0000", "#00ffff","#00ff00","#ff00ff","#ffffff","#ffff00"};
    public int color = 3;
    public int language = 0;
    // Load settings
    public com.klaus3d3.xDripwatchface.settings.APsettings settings;
    private Context Settingsctx;

    // Languages
    public static String[] codes = {
            "English", "中文", "Czech", "Nederlands", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "한국어", "Polski", "Português", "Русский", "Slovenčina", "Español", "Türkçe",
    };

    private static String[][] days = {
            //{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},
            {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},
            {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"},
            {"NEDĚLE","PONDĚLÍ", "ÚTERÝ", "STŘEDA", "ČTVRTEK", "PÁTEK", "SOBOTA"},
            {"ZONDAG", "MAANDAG", "DINSDAG", "WOENSDAG", "DONDERDAG", "VRIJDAG", "ZATERDAG"},
            {"DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI"},
            {"SONNTAG", "MONTAG", "DIENSTAG", "MITTWOCH", "DONNERSTAG", "FREITAG", "SAMSTAG"},
            {"ΚΥΡΙΑΚΉ", "ΔΕΥΤΈΡΑ", "ΤΡΊΤΗ", "ΤΕΤΆΡΤΗ", "ΠΈΜΠΤΗ", "ΠΑΡΑΣΚΕΥΉ", "ΣΆΒΒΑΤΟ"},
            {"ש'","ו'","ה'","ד'","ג'","ב'","א'"},
            {"VASÁRNAP", "HÉTFŐ", "KEDD", "SZERDA", "CSÜTÖRTÖK", "PÉNTEK", "SZOMBATON"},
            {"DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ", "GIOVEDÌ", "VENERDÌ", "SABATO"},
            {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"},
            {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"},
            {"NIEDZIELA", "PONIEDZIAŁEK", "WTOREK", "ŚRODA", "CZWARTEK", "PIĄTEK", "SOBOTA"},
            {"DOMINGO", "SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO"},
            {"ВОСКРЕСЕНЬЕ", "ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА"},
            {"NEDEĽA", "PONDELOK", "UTOROK", "STREDA", "ŠTVRTOK", "PIATOK", "SOBOTA"},
            {"DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"},
            {"PAZAR", "PAZARTESI", "SALı", "ÇARŞAMBA", "PERŞEMBE", "CUMA", "CUMARTESI"},
    };

    private static String[][] days_3let = {
            //{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},
            {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"},
            {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"},
            {"NE", "PO", "ÚT", "ST", "ČT", "PÁ", "SO"},
            {"ZON", "MAA", "DIN", "WOE", "DON", "VRI", "ZAT"},
            {"DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM"},
            {"SON", "MON", "DIE", "MIT", "DON", "FRE", "SAM"},
            {"ΚΥΡ", "ΔΕΥ", "ΤΡΙ", "ΤΕΤ", "ΠΕΜ", "ΠΑΡ", "ΣΑΒ"},
            {"ש'","ו'","ה'","ד'","ג'","ב'","א'"},
            {"VAS", "HÉT", "KED", "SZE", "CSÜ", "PÉN", "SZO"},
            {"DOM", "LUN", "MAR", "MER", "GIO", "VEN", "SAB"},
            {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"},
            {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"},
            {"NIE", "PON", "WTO", "ŚRO", "CZW", "PIĄ", "SOB"},
            {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SÁB"},
            {"ВОС", "ПОН", "ВТО", "СРЕ", "ЧЕТ", "ПЯТ", "СУБ"},
            {"NED", "PON", "UTO", "STR", "ŠTV", "PIA", "SOB"},
            {"DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB"},
            {"PAZ", "PAR", "SAL", "ÇAR", "PER", "CUM", "CUR"},
    };

    private static String[][] months = {
            //{"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},
            {"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},
            {"十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"},
            {"PROSINEC", "LEDEN", "ÚNOR", "BŘEZEN", "DUBEN", "KVĚTEN", "ČERVEN", "ČERVENEC", "SRPEN", "ZÁŘÍ", "ŘÍJEN", "LISTOPAD", "PROSINEC"},
            {"DECEMBER", "JANUARI", "FEBRUARI", "MAART", "APRIL", "MEI", "JUNI", "JULI", "AUGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DECEMBER"},
            {"DÉCEMBRE", "JANVIER", "FÉVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOÛT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DÉCEMBRE"},
            {"DEZEMBER", "JANUAR", "FEBRUAR", "MÄRZ", "APRIL", "MAI", "JUNI", "JULI", "AUGUST", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DEZEMBER"},
            {"ΔΕΚΈΜΒΡΙΟΣ", "ΙΑΝΟΥΆΡΙΟΣ", "ΦΕΒΡΟΥΆΡΙΟΣ", "ΜΆΡΤΙΟΣ", "ΑΠΡΊΛΙΟΣ", "ΜΆΙΟΣ", "ΙΟΎΝΙΟΣ", "ΙΟΎΛΙΟΣ", "ΑΎΓΟΥΣΤΟΣ", "ΣΕΠΤΈΜΒΡΙΟΣ", "ΟΚΤΏΒΡΙΟΣ", "ΝΟΈΜΒΡΙΟΣ", "ΔΕΚΈΜΒΡΙΟΣ"},
            {"דצמבר", "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"},
            {"DECEMBER", "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS", "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"},
            {"DICEMBRE", "GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO", "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"},
            {"12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"},
            {"12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"},
            {"GRUDZIEŃ", "STYCZEŃ", "LUTY", "MARZEC", "KWIECIEŃ", "MAJ", "CZERWIEC", "LIPIEC", "SIERPIEŃ", "WRZESIEŃ", "PAŹDZIERNIK", "LISTOPAD", "GRUDZIEŃ"},
            {"DEZEMBRO", "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"},
            {"ДЕКАБРЬ", "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"},
            {"DECEMBER", "JANUÁR", "FEBRUÁR", "MAREC", "APRÍL", "MÁJ", "JÚN", "JÚL", "AUGUST", "SEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"},
            {"DICIEMBRE", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"},
            {"ARALıK", "OCAK", "ŞUBAT", "MART", "NISAN", "MAYıS", "HAZIRAN", "TEMMUZ", "AĞUSTOS", "EYLÜL", "EKIM", "KASıM", "ARALıK"},
    };

    private static String[][] months_3let = {
            //{"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},
            {"DEC", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV"},
            {"十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月"},
            {"PRO", "LED", "ÚNO", "BŘE", "DUB", "KVĚ", "ČER", "ČER", "SRP", "ZÁŘ", "ŘÍJ", "LIS"},
            {"DEC", "JAN", "FEB", "MAA", "APR", "MEI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV"},
            {"DÉC", "JAN", "FÉV", "MAR", "AVR", "MAI", "JUI", "JUI", "AOÛ", "SEP", "OCT", "NOV"},
            {"DEZ", "JAN", "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV"},
            {"ΔΕΚ", "ΙΑΝ", "ΦΕΒ", "ΜΑΡ", "ΑΠΡ", "ΜΑΙ", "ΙΟΥΝ", "ΙΟΥΛ", "ΑΥΓ", "ΣΕΠ", "ΟΚΤ", "ΝΟΕ"},
            {"דצמ", "ינו", "פבר", "מרץ", "אפר", "מאי", "יונ", "יול", "אוג", "ספט", "אוק", "נוב"},
            {"DEC", "JAN", "FEB", "MÁR", "ÁPR", "MÁJ", "JÚN", "JÚL", "AUG", "SZE", "OKT", "NOV"},
            {"DIC", "GEN", "FEB", "MAR", "APR", "MAG", "GIU", "LUG", "AGO", "SET", "OTT", "NOV"},
            {"12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月"},
            {"12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월"},
            {"GRU", "STY", "LUT", "MAR", "KWI", "MAJ", "CZE", "LIP", "SIE", "WRZ", "PAŹ", "LIS"},
            {"DEZ", "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV"},
            {"ДЕК", "ЯНВ", "ФЕВ", "МАР", "АПР", "МАЙ", "ИЮН", "ИЮЛ", "АВГ", "СЕН", "ОКТ", "НОЯ"},
            {"DEC", "JAN", "FEB", "MAR", "APR", "MÁJ", "JÚN", "JÚL", "AUG", "SEP", "OKT", "NOV"},
            {"DIC", "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV"},
            {"ARA", "OCA", "ŞUB", "MAR", "NIS", "MAY", "HAZ", "TEM", "AĞU", "EYL", "EKI", "KAS"},
    };

    @Override
    public void init(Service service) {

        // Please do not change the following line
        Toast.makeText(service, "Code by GreatApo, style by "+service.getResources().getString(R.string.author), Toast.LENGTH_SHORT).show();
        try {
            Settingsctx=service.getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
        }catch(Exception e){Log.e("MainClock",e.toString());}

        this.settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);
        this.language = this.settings.get("lang", this.language) % this.codes.length;
        this.color = this.settings.getInt("color",this.color);

        this.background = service.getResources().getDrawable(R.drawable.background);
        this.background.setBounds(0, 0, 320, 300);

        this.leftHour = service.getResources().getDimension(R.dimen.hours_left);
        this.topHour = service.getResources().getDimension(R.dimen.hours_top);
        this.leftMinute = service.getResources().getDimension(R.dimen.minutes_left);
        this.topMinute = service.getResources().getDimension(R.dimen.minutes_top);
        this.leftSeconds = service.getResources().getDimension(R.dimen.seconds_left);
        this.topSeconds = service.getResources().getDimension(R.dimen.seconds_top);
        this.leftIndicator = service.getResources().getDimension(R.dimen.indicator_left);
        this.topIndicator = service.getResources().getDimension(R.dimen.indicator_top);
        this.leftDate = service.getResources().getDimension(R.dimen.date_left);
        this.topDate = service.getResources().getDimension(R.dimen.date_top);
        this.leftDay = service.getResources().getDimension(R.dimen.day_left);
        this.topDay = service.getResources().getDimension(R.dimen.day_top);
        this.leftMonth = service.getResources().getDimension(R.dimen.month_left);
        this.topMonth = service.getResources().getDimension(R.dimen.month_top);
        this.leftWeekday = service.getResources().getDimension(R.dimen.weekday_left);
        this.topWeekday = service.getResources().getDimension(R.dimen.weekday_top);
        this.leftYear = service.getResources().getDimension(R.dimen.year_left);
        this.topYear = service.getResources().getDimension(R.dimen.year_top);

        this.hourFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.hourFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.hourFont.setTextSize(service.getResources().getDimension(R.dimen.hours_font_size));
        this.hourFont.setColor(service.getResources().getColor(R.color.hour_colour));
        this.hourFont.setTextAlign(Paint.Align.CENTER);

        this.minutesFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.minutesFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.minutesFont.setTextSize(service.getResources().getDimension(R.dimen.minutes_font_size));
        this.minutesFont.setColor(service.getResources().getColor(R.color.minute_colour));
        this.minutesFont.setTextAlign(Paint.Align.CENTER);

        this.secondsBool = service.getResources().getBoolean(R.bool.seconds);
        this.secondsFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.secondsFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.secondsFont.setTextSize(service.getResources().getDimension(R.dimen.seconds_font_size));
        this.secondsFont.setColor(service.getResources().getColor(R.color.seconds_colour));
        this.secondsFont.setTextAlign(Paint.Align.CENTER);

        this.indicatorBool = service.getResources().getBoolean(R.bool.indicator);
        this.indicatorFlashBool = service.getResources().getBoolean(R.bool.flashing_indicator);
        this.indicatorFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.indicatorFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.indicatorFont.setTextSize(service.getResources().getDimension(R.dimen.indicator_font_size));
        this.indicatorFont.setColor(service.getResources().getColor(R.color.indicator_colour));
        this.indicatorFont.setTextAlign(Paint.Align.CENTER);

        this.dateBool = service.getResources().getBoolean(R.bool.date);
        this.dateAlignLeftBool = service.getResources().getBoolean(R.bool.date_left_align);
        this.dateFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.dateFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.dateFont.setTextSize(service.getResources().getDimension(R.dimen.date_font_size));
        this.dateFont.setColor(service.getResources().getColor(R.color.date_colour));
        this.dateFont.setTextAlign( (this.dateAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.weekdayBool = service.getResources().getBoolean(R.bool.week_name);
        this.three_letters_day_if_textBool = service.getResources().getBoolean(R.bool.three_letters_day_if_text);
        this.weekdayAlignLeftBool = service.getResources().getBoolean(R.bool.weekday_left_align);
        this.weekdayFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.weekdayFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.weekdayFont.setTextSize(service.getResources().getDimension(R.dimen.weekday_font_size));
        this.weekdayFont.setColor(service.getResources().getColor(R.color.weekday_colour));
        this.weekdayFont.setTextAlign( (this.weekdayAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.dayBool = service.getResources().getBoolean(R.bool.day);
        this.dayAlignLeftBool = service.getResources().getBoolean(R.bool.day_left_align);
        this.dayFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.dayFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.dayFont.setTextSize(service.getResources().getDimension(R.dimen.day_font_size));
        this.dayFont.setColor(service.getResources().getColor(R.color.day_colour));
        this.dayFont.setTextAlign( (this.dayAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.monthBool = service.getResources().getBoolean(R.bool.month);
        this.month_as_textBool = service.getResources().getBoolean(R.bool.month_as_text);
        this.three_letters_month_if_textBool = service.getResources().getBoolean(R.bool.three_letters_month_if_text);
        this.monthAlignLeftBool = service.getResources().getBoolean(R.bool.month_left_align);
        this.monthFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.monthFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.monthFont.setTextSize(service.getResources().getDimension(R.dimen.month_font_size));
        this.monthFont.setColor(service.getResources().getColor(R.color.month_colour));
        this.monthFont.setTextAlign( (this.monthAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.yearBool = service.getResources().getBoolean(R.bool.year);
        this.yearAlignLeftBool = service.getResources().getBoolean(R.bool.year_left_align);
        this.yearFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.yearFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.yearFont.setTextSize(service.getResources().getDimension(R.dimen.year_font_size));
        this.yearFont.setColor(service.getResources().getColor(R.color.year_colour));
        this.yearFont.setTextAlign( (this.yearAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.no_0_on_hour_first_digit = service.getResources().getBoolean(R.bool.no_0_on_hour_first_digit);
    }

    // Screen open watch mode
    @Override
    public void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {

        // Draw background image
        this.background.draw(canvas);

        // Draw hours
        //canvas.drawText( (this.no_0_on_hour_first_digit)?hours+"":Util.formatTime(hours), this.leftHour, this.topHour, this.hourFont);

        // Draw minutes
        //canvas.drawText(Util.formatTime(minutes), this.leftMinute, this.topMinute, this.minutesFont);

        // JAVA calendar get/show time library
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, week);

        // Draw Date
        if(this.dateBool) {
            String date = Util.formatTime(day)+"."+Util.formatTime(month)+"."+Integer.toString(year);//String.format("%02d.%02d.%02d", day, month, year);
            canvas.drawText(date, leftDate, topDate, this.dateFont);
        }

        // Draw Day
        if(this.dayBool) {
            String dayText = Util.formatTime(day);
            canvas.drawText(dayText, leftDay, topDay, this.dayFont);
        }

        // Get + Draw WeekDay (using JAVA)
        if(this.weekdayBool) {
            //String weekday = String.format("%S", new SimpleDateFormat("EE").format(calendar.getTime()));
            int weekdaynum = calendar.get(Calendar.DAY_OF_WEEK)-1;
            String weekday = (this.three_letters_day_if_textBool)? days_3let[this.language][weekdaynum] : days[this.language][weekdaynum] ;
            canvas.drawText(weekday, leftWeekday, topWeekday, this.weekdayFont);
        }

        // Draw Month
        if(this.monthBool) {
            String monthText = (this.month_as_textBool)? ( (this.three_letters_month_if_textBool)? months_3let[this.language][month] : months[this.language][month] ) : String.format("%02d", month) ;
            canvas.drawText(monthText, leftMonth, topMonth, this.monthFont);
        }

        // Draw Year
        if(this.yearBool) {
            canvas.drawText(Integer.toString(year), leftYear, topYear, this.yearFont);
        }

        // Draw Seconds
        if(this.secondsBool) {
            canvas.drawText(Util.formatTime(seconds), leftSeconds, topSeconds, this.secondsFont);
        }

        // : indicator Draw + Flashing
        if(this.indicatorBool) {
            String indicator = ":";
            if (seconds % 2 == 0 || !this.indicatorFlashBool) { // Draw only on even seconds (flashing : symbol)
                canvas.drawText(indicator, this.leftIndicator, this.topIndicator, this.indicatorFont);
            }
        }
    }



    // Screen locked/closed watch mode (Slpt mode)
    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        //Load Settings
        try {
            this.settings = new APsettings(Constants.PACKAGE_NAME, service.getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, 0));
        }catch(Exception e){
            Log.e("xDripwidget",e.toString());}
        this.language = this.settings.get("lang", this.language) % this.codes.length;
        this.color = this.settings.getInt("color",this.color);
        this.secondsBool = service.getResources().getBoolean(R.bool.seconds);
        int tmp_left;

        // Draw background image
        SlptPictureView background = new SlptPictureView();
        background.setImagePicture(SimpleFile.readFileFromAssets(service, "background_splt.png"));

        // Set font
        Typeface timeTypeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE);




        // Draw indicator
        SlptLinearLayout indicatorLayout = new SlptLinearLayout();
        SlptPictureView colon = new SlptPictureView();
        colon.setStringPicture(":");
        indicatorLayout.add(colon);
        indicatorLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.indicator_font_size),
                service.getResources().getColor(R.color.indicator_colour_slpt),
                timeTypeFace
        );
        // Position based on screen on
        indicatorLayout.alignX = 2;
        indicatorLayout.alignY=0;
        indicatorLayout.setRect(
                (int) (2*service.getResources().getDimension(R.dimen.indicator_left)+640),
                (int) (service.getResources().getDimension(R.dimen.indicator_font_size))
        );
        indicatorLayout.setStart(
                -320,
                (int) (service.getResources().getDimension(R.dimen.indicator_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.indicator_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.indicator)){indicatorLayout.show=false;}


       timeTypeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE);


        // Draw day of month
        SlptLinearLayout dayLayout = new SlptLinearLayout();
        dayLayout.add(new SlptDayHView());
        dayLayout.add(new SlptDayLView());
        dayLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.day_font_size),
                service.getResources().getColor(R.color.day_colour_slpt),
                timeTypeFace);
        // Position based on screen on
        dayLayout.alignX = 2;
        dayLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.day_left);
        if(!service.getResources().getBoolean(R.bool.day_left_align)) {
            // If text is centered, set rectangle
            dayLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.day_font_size))
            );
            tmp_left = -320;
        }
        dayLayout.setStart(
                tmp_left,
                (int) (service.getResources().getDimension(R.dimen.day_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.day_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.day)){dayLayout.show=false;}


        // Draw month
        SlptLinearLayout monthLayout = new SlptLinearLayout();
        //monthLayout.add(new SlptMonthHView());
        monthLayout.add(new SlptMonthLView());
        // Fix 00 type of month
        // JAVA calendar get/show time library
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        if(month>=9){
            months_3let[2] = months_3let[0];
            months_3let[0] = months_3let[10];
            months_3let[1] = months_3let[11];
            months[2] = months_3let[0];
            months[0] = months_3let[10];
            months[1] = months_3let[11];
        }
        if(service.getResources().getBoolean(R.bool.month_as_text)) { // if as text
            if (service.getResources().getBoolean(R.bool.three_letters_month_if_text)) {
                monthLayout.setStringPictureArrayForAll(months_3let[language]);
            } else {
                monthLayout.setStringPictureArrayForAll(months[language]);
            }
        }
        monthLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.month_font_size),
                service.getResources().getColor(R.color.month_colour_slpt),
                timeTypeFace);
        // Position based on screen on
        monthLayout.alignX = 2;
        monthLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.month_left);
        if(!service.getResources().getBoolean(R.bool.month_left_align)) {
            // If text is centered, set rectangle
            monthLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.month_font_size))
            );
            tmp_left = -320;
        }
        monthLayout.setStart(
                tmp_left,
                (int) (service.getResources().getDimension(R.dimen.month_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.month_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.month)){monthLayout.show=false;}



        // Set day name font
        Typeface weekfont = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE);

        // Draw day name
        SlptLinearLayout WeekdayLayout = new SlptLinearLayout();
        WeekdayLayout.add(new SlptWeekView());
        if(service.getResources().getBoolean(R.bool.three_letters_day_if_text)){
            WeekdayLayout.setStringPictureArrayForAll(days_3let[language]);
        }else{
            WeekdayLayout.setStringPictureArrayForAll(days[language]);
        }
        WeekdayLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.weekday_font_size),
                service.getResources().getColor(R.color.weekday_colour_slpt),
                weekfont
        );
        // Position based on screen on
        WeekdayLayout.alignX = 2;
        WeekdayLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.weekday_left);
        if(!service.getResources().getBoolean(R.bool.weekday_left_align)) {
            // If text is centered, set rectangle
            WeekdayLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.weekday_font_size))
            );
            tmp_left = -320;
        }
        WeekdayLayout.setStart(
                tmp_left,
                (int) (service.getResources().getDimension(R.dimen.weekday_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.weekday_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.week_name)){WeekdayLayout.show=false;}

        return Arrays.asList(background, indicatorLayout, dayLayout, monthLayout, WeekdayLayout);
    }
}
