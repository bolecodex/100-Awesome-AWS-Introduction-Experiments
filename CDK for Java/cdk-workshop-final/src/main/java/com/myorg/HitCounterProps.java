package com.myorg;

import software.amazon.awscdk.services.lambda.IFunction;

// In Java, an interface is an abstract type that contains a collection of methods and constant variables. 
// 接口，在Java编程语言中是一个抽象类型，它被用来要求类必须实现指定的方法，使不同类的对象可以利用相同的界面进行沟通。
// 定义HitCounterProps这个接口的getDownstream方法，调用方法props.getDownstream()，传递所需用的IFunction
public interface HitCounterProps {
    
    // Public constructor for the props builder 获取HitCounterProps的构造器Builder
    // Builder是HitCounterProps接口中自定义的内部类
    public static Builder builder() {
        return new Builder();
    }

    // Referencing resources: If a construct property represents another AWS construct, 
    // its type is that of the interface type of that construct. 
    // For example, the Amazon ECS service takes a property cluster of type ecs.ICluster; 
    // the CloudFront distribution takes a property sourceBucket (Python: source_bucket) of type s3.IBucket.

    // getDownstream是HitCounterProps的方法，返回类型为IFunction
    // The function for which we want to count url hits
    IFunction getDownstream();

    // Java.lang package contains the classes that are fundamental to the design of the Java programming language. 
    // Number类属于标准库，默认就已经导入
    Number getReadCapacity();

    // The builder for the props interface，通过静态类定义HitCounterProps的构造器Builder
    // 调用时的写法：HitCounterProps Props = HitCounterProps.Builder.downstream(function).build();
    public static class Builder {
        private IFunction downstream;
        private Number readCapacity;

        // 从外部传入function并赋值给downstream，返回的类型是Builder
        public Builder downstream(final IFunction function) {
            this.downstream = function;
            return this;
        }
        public Builder readCapacity(final Number readCapacity) {
            this.readCapacity = readCapacity;
            return this;
        }

        public HitCounterProps build() {

            //如果downstream为空，否则抛出空指针异常
            if(this.downstream == null) {
                throw new NullPointerException("The downstream property is required!");
            }

            // 接口和抽象类无法实例化，因为内部有尚未完全声明的方法，如果一定要实例化，则需完全实现内部的方法
            // 实例化HitCounterProps接口并返回
            return new HitCounterProps() {
                
                //@Override注解：HitCounterProps是接口，实现这个接口的getDownstream方法
                @Override
                public IFunction getDownstream() {
                    return downstream;
                }

                @Override
                public Number getReadCapacity() {
                    return readCapacity;
                }
            };
        }
    }
}
