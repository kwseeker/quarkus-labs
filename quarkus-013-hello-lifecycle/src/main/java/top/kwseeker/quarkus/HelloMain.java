///*
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package top.kwseeker;
//
//import io.quarkus.runtime.Quarkus;
//import io.quarkus.runtime.QuarkusApplication;
//import io.quarkus.runtime.annotations.QuarkusMain;
//
//@QuarkusMain
//public class HelloMain implements QuarkusApplication {
//
//    @Override
//    public int run(String... args) {
//        if(args.length>0) {
//        	System.out.println("hi,command mode,this is args:" + args);
//        } else {
//        	System.out.println("hi,command mode");
//        }
//        //Quarkus.waitForExit();  // 要如果要使用 @QuarkusMain 自定义“主类”，需要使用 Quarkus.waitForExit() 避免其Web组件自动退出
//        return 0;
//    }
//
//
//    public static void main(String... args) {
//        Quarkus.run(HelloMain.class, args);
//    }
//
//
//}
