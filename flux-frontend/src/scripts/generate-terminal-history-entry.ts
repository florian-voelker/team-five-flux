import { LoremIpsum } from 'lorem-ipsum';
import { TerminalChooseHistoryEntry } from "./store/state-terminal-history";
import { Interaction } from './store/state-current-interaction';

// taken from https://gist.github.com/blixt/f17b47c62508be59987b
class PRNG {
    private seed: number;

    constructor(seed: number) {
        seed |= 0;
        seed %= (2 ** 31 - 1);

        if (seed <= 0)
            seed += (2 ** 31 - 2);

        this.seed = seed;
    }

    next() {
        return this.seed = this.seed * 48271 % (2 ** 31 - 1);
    }

    nextFloat() {
        return (this.next() - 1) / (2 ** 31 - 2);
    }

    randomInteger(min: number, max: number, inclusive = false) {
        min = Math.ceil(min);
        max = Math.floor(max);

        return Math.floor(this.nextFloat() * (max - min + (inclusive as any))) + min;
    }
}

export class HistoryEntryGenerator {
    private readonly prng = new PRNG(50000);
    private readonly lorem = new LoremIpsum({
        wordsPerSentence: {
            min: 2, max: 5
        },
        random: () => this.prng.nextFloat()
    });

    generate(id: number, maxOptions = 5, minOptions = 2) {
        const optionAmount = this.prng.randomInteger(minOptions, maxOptions);
        const choose = this.prng.randomInteger(0, optionAmount, false);

        const options = [];

        for (let o = 0; o < optionAmount; o++)
            options.push(this.lorem.generateSentences(1));

        return { options, choose, id } as TerminalChooseHistoryEntry;
    }

    generateSeveral(amount: number) {
        const samples = [] as TerminalChooseHistoryEntry[];

        const ite = this[Symbol.iterator]();
        while (amount-- > 0)
            samples.push(ite.next().value as TerminalChooseHistoryEntry);

        return samples;
    }

    [Symbol.iterator]() { return this.startWithIndex(); }

    *startWithIndex(index = 0) {
        while (true)
            yield this.generate(index++);
    }
}

export class InteractionGenerator {
    private readonly prng = new PRNG(50000);
    private readonly loremOptions = new LoremIpsum({
        wordsPerSentence: {
            min: 3, max: 12
        },
        random: () => this.prng.nextFloat()
    });

    private readonly loremInformation = new LoremIpsum({
        wordsPerSentence: {
            min: 5, max: 12
        },
        random: () => this.prng.nextFloat()
    });

    genOption() {
        return this.loremOptions.generateSentences(1);
    }

    genInformation() {
        return this.loremInformation.generateSentences(1);
    }

    generate(maxOptions = 5, minOptions = 2) {
        const optionAmount = this.prng.randomInteger(minOptions, maxOptions);

        const options = [];

        for (let o = 0; o < optionAmount; o++)
            options.push(this.genOption());

        return { information: this.genInformation(), options } as Interaction;
    }
}

export class InventoryGenerator {
    private readonly prng = new PRNG(0xFFEE99);

    private readonly loremItem = new LoremIpsum({
        wordsPerSentence: {
            min: 1, max: 2
        },
        random: () => this.prng.nextFloat()
    });

    *[Symbol.iterator]() {
        while (true)
            yield this.loremItem.generateSentences(1);
    }

    generateRandomItems(minItems = 2, maxItems = 10) {
        const itemAmount = this.prng.randomInteger(minItems, maxItems);

        const items = [];
        for (let i = 0; i < itemAmount; i++) {
            let name = this.loremItem.generateWords(this.prng.nextFloat() < 0.2 ? 2 : 1);

            name = name.replace(/(?:^| )(.)/g, (c, v) =>
                (c.length > 1 ? ' ' : '') + v.toUpperCase()
            )

            items.push({
                id: i,
                name
            })
        }

        return items;
    }
}