package com.hddata.technologystudy;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    final String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1483633128024.jpg";
    private String TAG = "RxJava";
    private ImageView iv;
    private String[] testArray = new String[]{"1", "2", "3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        rxThread();

    }


    /**
     * 基础写法
     */
    private void rxBase(){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onComplete();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        };

        observable.subscribe(observer);

    }

    /**
     * 链式写法
     */
    private void rxLine(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });

        /**
         * 只接受onNext事件
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("world");
                e.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.d(TAG, s);
                Log.d(TAG, Thread.currentThread().getName());
            }
        });
    }

    /**
     * 线程切换
     * Observable可以多次切换线程，但只有第一个有效
     * 而Observer则可以多次切换，最后一个有效
     */
    private void rxThread(){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "Observable is onThread:" +Thread.currentThread().getName());
                e.onNext("hello");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "Observer is onThread:" +Thread.currentThread().getName());                    }
                });

    }




    //    以下为RxJava Version 1的使用方式
//    /**
//     * 基础使用方式，详细调用过程
//     */
//    private void rxBase() {
//        //创建observer，即观察者
//        Observer<String> observer = new Observer<String>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG, "complete");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d(TAG, s);
//            }
//        };
//
//        //创建observable，即被观察者
//        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("hello");
//                subscriber.onNext("world");
//                subscriber.onCompleted();
//            }
//        });
//
//
//        /** 快捷创建事件序列
//         *  将会依次调用：
//         onNext("hello");
//         onNext("world");
//         onCompleted();
//         或者使用Observable.from(T[])
//         */
//        Observable ob = Observable.just("hello", "world");
//
//
//        //建立订阅关系
//        observable.subscribe(observer);
//        ob.subscribe(observer);
//    }
//
//
//    /**
//     * 线程调用方式
//     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
//     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
//     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
//     * 区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread()
//     * 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
//     * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，
//     * 例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
//     * 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
//     * *** Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行
//     */
//    private void rxThread() {
//
//        /**
//         * 不指定线程，默认的Scheduler
//         */
//        String[] names = new String[]{"orange", "and", "apple"};
//        Observable.from(names).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.d(TAG, s);
//            }
//        });
//
//        /**
//         * 指定subscribe()发生在IO线程
//         * 指定subscriber的回调发生在主线程
//         */
//        Observable.from(names)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.d(TAG, s);
//                    }
//                });
//
//        /**
//         * example 根据图片路径显示图片
//         * IO读取图片，在主线程即界面显示
//         */
//
//        Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                File file = new File(imagePath);
//                subscriber.onNext(file);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<File>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "图片成功加载");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(File f) {
//                        showImage(f);
//                    }
//                });
//
//    }
//
//
//    /**
//     * 数据变换
//     */
//    private void rxFunc() {
//
//        /**
//         * map() 方法将参数中的 String 对象转换成一个 File 对象后返回，而在经过 map() 方法后，事件的参数类型也由 String 转为了 File
//         */
//        Observable.just(imagePath)
//                .map(new Func1<String, File>() {
//                    @Override
//                    public File call(String s) {
//                        return getFileFromPath(s);
//                    }
//                })
//                .subscribe(new Action1<File>() {
//                    @Override
//                    public void call(File file) {
//                        showImage(file);
//                    }
//                });
//
//
//        /*测试数据*打印学生的课程*/
//        List<String> course = new ArrayList<>();
//        course.add("English");
//        course.add("Math");
//        course.add("Chinese");
//        Student[] students = new Student[]{new Student("jack", course), new Student("orange", course)};
//
//        //不转换
//        Observable.from(students).subscribe(new Action1<Student>() {
//            @Override
//            public void call(Student student) {
//                for (String c : student.getCourse()) {
//                    Log.d(TAG, c);
//                }
//            }
//        });
//
//        //map()方式
//        Observable.from(students)
//                .map(new Func1<Student, List<String>>() {
//                    @Override
//                    public List<String> call(Student student) {
//                        return student.getCourse();
//                    }
//                })
//                .subscribe(new Action1<List<String>>() {
//                    @Override
//                    public void call(List<String> course) {
//                        for (String c : course) {
//                            Log.d(TAG, c);
//                        }
//                    }
//                });
//
//        //flatMap()方式，无需使用for循环
//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(Student student) {
//                        return Observable.from(student.getCourse());
//                    }
//                })
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.d(TAG, "*" + s);
//                    }
//                });
//
//
//    }
//
    private File getFileFromPath(String imagePath) {
        return new File(imagePath);
    }

    private <T> void showImage(T t) {
        Glide.with(MainActivity.this).load(t).asBitmap().into(iv);
    }


}
