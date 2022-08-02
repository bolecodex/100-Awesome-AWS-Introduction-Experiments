package com.myorg;

import software.amazon.awscdk.services.lambda.IFunction;

public interface HitCounterProps {
    public static Builder builder() {
        return new Builder();
    }

    IFunction getDownstream();

    Number getReadCapacity();

    public static class Builder {
        private IFunction downstream;
        private Number readCapacity;

        public Builder downstream(final IFunction function) {
            this.downstream = function;
            return this;
        }
        public Builder readCapacity(final Number readCapacity) {
            this.readCapacity = readCapacity;
            return this;
        }

        public HitCounterProps build() {
            if (this.downstream == null) {
                throw new NullPointerException("The downstream property is required");
            }
            return new HitCounterProps() {
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