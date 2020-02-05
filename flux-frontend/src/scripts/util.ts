import { Reducer } from "react";

interface ArgumentReturnCombiner<T extends (...args: any) => any> {
    (ret: ReturnType<T>, ...args: Parameters<T>): Parameters<T>
}

export function chainFunctions<T extends (...args: any) => any>(resultToArgTransformer: ArgumentReturnCombiner<T>, ...functions: T[]): T {
    return ((...args: Parameters<T>) => {
        let result: ReturnType<T>;

        for (let i = 0; i < functions.length; i++) {
            const fnc = functions[i];

            args = !result ? args : resultToArgTransformer(result, ...args);

            try {
                result = fnc.apply(null, args);
            } catch (e) {
                throw new Error('the combined function (number ' + i + (fnc.name ? ', named: ' + fnc.name : '') + ') threw an error while processing the params: ' + args + '\n' + e);
            }
        }

        return result;
    }) as T;
}

export function chainReducer<T extends Reducer<any, any>>(...reducer: T[]) {
    return chainFunctions((v, ...s) => (s[0] = v, s), ...reducer);
}