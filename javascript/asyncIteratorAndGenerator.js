// ASYNC ITERATOR
// JS supports async iterators, i. e. iterators that returns promises instead of value
// You should return it from the [Symbol.asyncIterator] method.
// You can loop through this iterator with the for await (let x of object) loop.
class Range {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }

    [Symbol.asyncIterator]() {
        return {
            current: this.start,
            end: this.end,
            async next() {
                await new Promise((resolve) => setTimeout(resolve, 1000));
                return this.current <= this.end
                    ? { done: false, value: this.current++ }
                    : { done: true };
            },
        };
    }
}

let range = new Range(4, 9);
for await (let x of range) {
    console.log(x);
}



// ASYNC GENERATORS
// JS supports async generators, i. e. generators that returns promises
// You can use for await loop to iterator over the values.
async function* rangeGenerator(start, end) {
    for (let i = start; i <= end; i++) {
        await new Promise((resolve) => setTimeout(resolve, 1000));
        yield i;
    }
}

for await (let x of rangeGenerator(3, 9)) {
    console.log(x);
}



// ASYNC ITERATOR WITH GENARATOR
// You can implement [Symbol.asyncIterator] method with generator.
class Range {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }

    async *[Symbol.asyncIterator]() {
        for (let i = this.start; i <= this.end; i++) {
            await new Promise((resolve) => setTimeout(resolve, 1000));
            yield i;
        }
    }
}

let range = new Range(4, 9);
for await (let x of range) {
    console.log(x);
}