import java.util.*;

class Complex {
    double re, im;

    Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    Complex sub(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    Complex mul(Complex c) {
        return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    Complex div(Complex c) {
        double d = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / d, (im * c.re - re * c.im) / d);
    }

    public String toString() {
        return re + (im >= 0 ? "+" : "") + im + "i";
    }
}

class Matrix {
    Complex[][] a;
    int rows, cols;

    Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        a = new Complex[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                a[i][j] = new Complex(0, 0);
    }

    Matrix add(Matrix b) {
        if (rows != b.rows || cols != b.cols)
            return null;

        Matrix c = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                c.a[i][j] = a[i][j].add(b.a[i][j]);

        return c;
    }

    Matrix multiply(Matrix b) {
        if (cols != b.rows)
            return null;

        Matrix c = new Matrix(rows, b.cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < b.cols; j++) {
                Complex sum = new Complex(0, 0);

                for (int k = 0; k < cols; k++)
                    sum = sum.add(a[i][k].mul(b.a[k][j]));

                c.a[i][j] = sum;
            }
        }

        return c;
    }

    Matrix divide(Matrix b) {
        if (rows != b.rows || cols != b.cols)
            return null;

        Matrix c = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (b.a[i][j].re == 0 && b.a[i][j].im == 0)
                    return null;

                c.a[i][j] = a[i][j].div(b.a[i][j]);
            }
        }

        return c;
    }

    Matrix transpose() {
        Matrix c = new Matrix(cols, rows);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                c.a[j][i] = a[i][j];

        return c;
    }

    Complex determinant() {
        if (rows != cols)
            return null;

        if (rows == 1)
            return a[0][0];

        Complex result = new Complex(0, 0);

        for (int j = 0; j < cols; j++) {

            Matrix m = new Matrix(rows - 1, cols - 1);

            for (int i = 1; i < rows; i++) {
                int col = 0;

                for (int k = 0; k < cols; k++) {
                    if (k != j) {
                        m.a[i - 1][col] = a[i][k];
                        col++;
                    }
                }
            }

            Complex t = a[0][j].mul(m.determinant());

            if (j % 2 == 0)
                result = result.add(t);
            else
                result = result.sub(t);
        }

        return result;
    }

    public String toString() {
        String s = "";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                s += a[i][j] + " ";
            s += "\n";
        }

        return s;
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);

    static int readInt(String text) {
        while (true) {
            try {
                System.out.print(text);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Некорректный ввод");
            }
        }
    }

    static double readDouble(String text) {
        while (true) {
            try {
                System.out.print(text);
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Некорректный ввод");
            }
        }
    }

    static Matrix create() {

        int r;

        do {
            r = readInt("Строки: ");
            if (r <= 0)
                System.out.println("Размер должен быть больше 0");
        } while (r <= 0);

        int c;

        do {
            c = readInt("Столбцы: ");
            if (c <= 0)
                System.out.println("Размер должен быть больше 0");
        } while (c <= 0);


        Matrix m = new Matrix(r, c);

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {

                double re = readDouble("Элемент [" + i + "][" + j + "] real: ");
                double im = readDouble("imag: ");

                m.a[i][j] = new Complex(re, im);
            }
        }

        return m;
    }

    public static void main(String[] args) {

        Matrix A = null;
        Matrix B = null;

        while (true) {

            System.out.println("1 Создать матрицы");
            System.out.println("2 Сложение");
            System.out.println("3 Умножение");
            System.out.println("4 Деление");
            System.out.println("5 Транспонирование");
            System.out.println("6 Определитель");
            System.out.println("0 Выход");

            int x = readInt("> ");

            if (x == 0)
                break;

            if (x == 1) {
                System.out.println("Матрица A");
                A = create();

                System.out.println("Матрица B");
                B = create();
            }

            if (A == null) {
                System.out.println("Сначала создайте матрицы");
                continue;
            }

            if (x == 2) {
                Matrix r = A.add(B);
                System.out.println(r == null ? "Ошибка размеров" : r);
            }

            if (x == 3) {
                Matrix r = A.multiply(B);
                System.out.println(r == null ? "Ошибка размеров" : r);
            }

            if (x == 4) {
                Matrix r = A.divide(B);
                System.out.println(r == null ? "Ошибка деления" : r);
            }

            if (x == 5)
                System.out.println(A.transpose());

            if (x == 6) {
                Complex d = A.determinant();
                System.out.println(d == null ? "Матрица не квадратная" : d);
            }
        }
    }
}
