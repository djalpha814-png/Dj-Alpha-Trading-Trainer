package com.djalpha.tradingtrainer;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout box; TextView title, content; int lesson=0, score=0;
    String[] lessons = {
        "1. TREND: EMA 50 vs EMA 200\nIf EMA 50 is above EMA 200, the basic trend is bullish. If below, bearish.\n\nPractice: On MT5 M5, add EMA 50 and EMA 200 and watch their relationship.",
        "2. RSI 14\nRSI measures momentum. Our practice strategy uses the 50 level: crossing above 50 supports BUY; crossing below 50 supports SELL.\n\nNever use RSI alone.",
        "3. BUY SETUP\nEMA 50 > EMA 200 + price above EMA 50 + RSI crosses above 50 + bullish candle closes = BUY candidate.\n\nWait for candle close.",
        "4. SELL SETUP\nEMA 50 < EMA 200 + price below EMA 50 + RSI crosses below 50 + bearish candle closes = SELL candidate.\n\nWait for candle close.",
        "5. RISK MANAGEMENT\nThe example EA risks 1% per trade, uses ATR for Stop Loss and 1.5R Take Profit. No martingale or grid.\n\nPractice on DEMO first.",
        "6. M5 DISCIPLINE\nM5 is noisy. Avoid chasing a signal after a large candle. Confirm the closed candle, trend and risk before entering."
    };

    @Override public void onCreate(Bundle b){ super.onCreate(b); showHome(); }

    TextView tv(String s,int size){ TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setPadding(24,18,24,18); return t; }
    Button btn(String s){ Button b=new Button(this); b.setText(s); b.setAllCaps(false); return b; }

    void base(String heading){
        box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(20,20,20,20);
        ScrollView sv=new ScrollView(this); sv.addView(box); setContentView(sv);
        title=tv(heading,26); title.setTextColor(Color.rgb(20,70,120)); box.addView(title);
    }
    void showHome(){
        base("DJ Alpha Trading Trainer");
        box.addView(tv("XAUUSD + BTCUSD • M5 • DEMO LEARNING",16));
        box.addView(tv("Learn the exact logic behind your practice EA. This app does NOT connect to MT5 and does NOT place trades.",16));
        Button l=btn("📚 Start lessons"); l.setOnClickListener(v->showLesson(0)); box.addView(l);
        Button q=btn("🧠 Take BUY/SELL quiz"); q.setOnClickListener(v->quiz()); box.addView(q);
        Button r=btn("💰 Risk calculator"); r.setOnClickListener(v->calculator()); box.addView(r);
    }
    void showLesson(int n){
        lesson=n; base("Lesson "+(n+1)+" of "+lessons.length);
        box.addView(tv(lessons[n],18));
        if(n>0){Button p=btn("← Previous"); p.setOnClickListener(v->showLesson(n-1)); box.addView(p);}
        if(n<lessons.length-1){Button x=btn("Next →"); x.setOnClickListener(v->showLesson(n+1)); box.addView(x);}
        Button h=btn("Home"); h.setOnClickListener(v->showHome()); box.addView(h);
    }
    void quiz(){
        base("BUY / SELL Quiz");
        score=0; ask(0);
    }
    void ask(int n){
        String[] qs={
          "EMA 50 is above EMA 200, price is above EMA 50, RSI crosses above 50 and the candle closes bullish. Candidate?",
          "EMA 50 is below EMA 200, price is below EMA 50, RSI crosses below 50 and the candle closes bearish. Candidate?",
          "RSI is 48 and EMA 50 is above EMA 200. Is that enough by itself to enter BUY?"
        };
        if(n>=qs.length){ box.addView(tv("Score: "+score+"/"+qs.length+"\n\nGood practice. A quiz result is not a trading guarantee.",20)); Button h=btn("Home"); h.setOnClickListener(v->showHome()); box.addView(h); return; }
        box.addView(tv(qs[n],19));
        Button buy=btn("🟢 BUY"); Button sell=btn("🔴 SELL"); Button wait=btn("⏸ WAIT");
        box.addView(buy); box.addView(sell); box.addView(wait);
        final int idx=n;
        buy.setOnClickListener(v->{ if(idx==0)score++; if(idx==2)score=score; Toast.makeText(this, idx==0?"Correct":"Review the rules",Toast.LENGTH_SHORT).show(); clearAndAsk(idx+1);});
        sell.setOnClickListener(v->{ if(idx==1)score++; Toast.makeText(this,idx==1?"Correct":"Review the rules",Toast.LENGTH_SHORT).show(); clearAndAsk(idx+1);});
        wait.setOnClickListener(v->{ if(idx==2)score++; Toast.makeText(this,idx==2?"Correct":"Review the rules",Toast.LENGTH_SHORT).show(); clearAndAsk(idx+1);});
    }
    void clearAndAsk(int n){ box.removeAllViews(); box.addView(tv("Question "+(n+1),20)); ask(n); }
    void calculator(){
        base("Risk Calculator");
        EditText bal=new EditText(this); bal.setHint("Account balance (e.g. 1000)"); bal.setInputType(2|8192); box.addView(bal);
        EditText pct=new EditText(this); pct.setHint("Risk % (default 1)"); pct.setInputType(2|8192); box.addView(pct);
        Button c=btn("Calculate"); box.addView(c);
        TextView out=tv("",18); box.addView(out);
        c.setOnClickListener(v->{try{double b=Double.parseDouble(bal.getText().toString()); String ps=pct.getText().toString(); double p=ps.isEmpty()?1:Double.parseDouble(ps); out.setText(String.format(Locale.US,"Risk amount: %.2f\n\nThis is the maximum money you planned to risk for the trade. It is NOT a lot-size calculator because XAUUSD/BTCUSD contract specifications vary by broker.",b*p/100));}catch(Exception e){out.setText("Enter valid numbers.");}});
        Button h=btn("Home"); h.setOnClickListener(v->showHome()); box.addView(h);
    }
}