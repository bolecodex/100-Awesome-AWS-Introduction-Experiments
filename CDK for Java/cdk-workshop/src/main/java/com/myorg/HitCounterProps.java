package com.myorg;

import software.amazon.awscdk.services.lambda.IFunction;

// 定义HitCounterProps这个接口的getDownstream方法，调用方法props.getDownstream()，传递所需用的IFunction
public interface HitCounterProps {
    
    // Public constructor for the props builder 获取HitCounterProps的构造器Builder
    public static Builder builder() {
        return new Builder();
    }

    // getDownstream是HitCounterProps的方法，返回类型为IFunction
    // The function for which we want to count url hits
    IFunction getDownstream();

    // The builder for the props interface，通过静态类定义HitCounterProps的构造器Builder
    // 调用时的写法：HitCounterProps Props = HitCounterProps.Builder.downstream(function).build();
    public static class Builder {
        private IFunction downstream;

        // 从外部传入function并赋值给downstream，返回的类型是Builder
        public Builder downstream(final IFunction function) {
            this.downstream = function;
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
                
                //@Override注解：实现getDownstream这个方法
                @Override
                public IFunction getDownstream() {
                    return downstream;
                }
            };
        }
    }
}
