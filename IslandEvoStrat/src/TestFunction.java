import static java.lang.Math.*;

public interface TestFunction {
    double calculateFitness(double[] solution);
}

class SphereFunction implements TestFunction {
    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        for (double x: solution) {
            sum += pow(x, 2);
        }
        return sum;
    }
}

class RastriginFunction implements TestFunction {
    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        for (double x : solution) {
            sum += pow(x, 2) - 10.0 * cos(2 * Math.PI * x);
        }
        return 10.0 * solution.length + sum;
    }
}

class GriewankaFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        double il = 1.0;
        for (int i = 1; i <= solution.length; i++) {
            sum += pow(solution[i-1], 2)/4000;
            il *= cos(solution[i-1]/sqrt(i));
        }
        return sum - il + 1;
    }
}

class RosenbrockBananaFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        int solution_size = solution.length - 1;
        for (int i = 1; i <= solution_size; i++) {
            double sum1 = 100 * pow(solution[i] - pow(solution[i-1], 2), 2);
            double sum2 = pow(1 - solution[i-1], 2);
            sum += sum1 + sum2;
        }
        return sum;
    }
}

class AckleyFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        for (double x: solution) {
            sum1 += pow(x, 2);
            sum2 += cos(2*PI*x);
        }
        double term1 = exp(-0.2 * sqrt(sum1 / solution.length));
        double term2 = exp(sum2 / solution.length);
        double result = -20 * term1 - term2 + 20.0 + E;
        return result;
    }
}

class SchwefelFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        int n = solution.length;
        double param = 418.9829 * n;
        double sum = 0.0;
        for(double x: solution) {
            sum += x*sin(sqrt(abs(x)));
        }
        double result = param - sum;
        return result;
    }
}

class MichalewiczFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        for (int i = 1; i <= solution.length; i++) {
            sum += sin(solution[i-1]) * sin(pow(i * pow(solution[i-1], 2) / PI, 2*10));
        }
        return -sum;
    }
}

class ZakharovFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        for (int i = 1; i <= solution.length; i++) {
            sum1 += pow(pow(solution[i-1], 2) + 0.5*i*solution[i-1], 2);
            sum2 += 0.5 * i * solution[i-1];
            sum3 += 0.5 * i * solution[i-1];
        }
        double result = sum1 + pow(sum2, 2) + pow(sum3, 4);
        return result;
    }
}

class ShubertFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double result = 1.0;
        for (int i = 0; i < solution.length; i++) {
            double sum = 0.0;
            for (int j = 1; j <= 5; j++) {
                sum += j * cos((j + 1) * solution[i] + j);
            }
            result *= sum;
        }
        return result;
    }
}

class Zakharov1Function implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        for (int i = 1; i <= solution.length; i++) {
            sum1 += pow(solution[i-1], 2);
            sum2 += i*solution[i-1];
            sum3 += i*solution[i-1];
        }
        double result = sum1 + pow(0.5*sum2, 2) + pow(0.5*sum3, 4);
        return result;
    }
}

class SquareFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        for (double x: solution) {
            sum += pow(x - 3, 2);
        }
        return -sum;
    }
}

class LinearFunction implements TestFunction {

    @Override
    public double calculateFitness(double[] solution) {
        double sum = 0.0;
        for (double x: solution) {
            sum += 2 * x - 1;
        }
        return sum;
    }
}
