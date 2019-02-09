// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

// Referenced classes of package net.minecraft.src:
//            J_InvalidSyntaxException, J_PositionTrackingPushbackReader, J_JsonListener

public final class J_SajParser
{

    public J_SajParser()
    {
    }

    public void func_27463_a(Reader reader, J_JsonListener j_jsonlistener)
        throws J_InvalidSyntaxException, IOException
    {
        J_PositionTrackingPushbackReader j_positiontrackingpushbackreader = new J_PositionTrackingPushbackReader(reader);
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        switch(c)
        {
        case 123: // '{'
            j_positiontrackingpushbackreader.func_27334_a(c);
            j_jsonlistener.func_27195_b();
            func_27453_b(j_positiontrackingpushbackreader, j_jsonlistener);
            break;

        case 91: // '['
            j_positiontrackingpushbackreader.func_27334_a(c);
            j_jsonlistener.func_27195_b();
            func_27455_a(j_positiontrackingpushbackreader, j_jsonlistener);
            break;

        default:
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either [ or { but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
        int i = func_27448_l(j_positiontrackingpushbackreader);
        if(i != -1)
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Got unexpected trailing character [").append((char)i).append("].").toString(), j_positiontrackingpushbackreader);
        } else
        {
            j_jsonlistener.func_27204_c();
            return;
        }
    }

    private void func_27455_a(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
        throws J_InvalidSyntaxException, IOException
    {
        char c = (char)func_27448_l(j_positiontrackingpushbackreader);
        if(c != '[')
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object to start with [ but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
        j_jsonlistener.func_27200_d();
        char c1 = (char)func_27448_l(j_positiontrackingpushbackreader);
        j_positiontrackingpushbackreader.func_27334_a(c1);
        if(c1 != ']')
        {
            func_27464_d(j_positiontrackingpushbackreader, j_jsonlistener);
        }
        boolean flag = false;
        do
        {
            if(flag)
            {
                break;
            }
            char c2 = (char)func_27448_l(j_positiontrackingpushbackreader);
            switch(c2)
            {
            case 44: // ','
                func_27464_d(j_positiontrackingpushbackreader, j_jsonlistener);
                break;

            case 93: // ']'
                flag = true;
                break;

            default:
                throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either , or ] but got [").append(c2).append("].").toString(), j_positiontrackingpushbackreader);
            }
        } while(true);
        j_jsonlistener.func_27197_e();
    }

    private void func_27453_b(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
        throws J_InvalidSyntaxException, IOException
    {
        char c = (char)func_27448_l(j_positiontrackingpushbackreader);
        if(c != '{')
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object to start with { but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
        j_jsonlistener.func_27194_f();
        char c1 = (char)func_27448_l(j_positiontrackingpushbackreader);
        j_positiontrackingpushbackreader.func_27334_a(c1);
        if(c1 != '}')
        {
            func_27449_c(j_positiontrackingpushbackreader, j_jsonlistener);
        }
        boolean flag = false;
        do
        {
            if(flag)
            {
                break;
            }
            char c2 = (char)func_27448_l(j_positiontrackingpushbackreader);
            switch(c2)
            {
            case 44: // ','
                func_27449_c(j_positiontrackingpushbackreader, j_jsonlistener);
                break;

            case 125: // '}'
                flag = true;
                break;

            default:
                throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either , or } but got [").append(c2).append("].").toString(), j_positiontrackingpushbackreader);
            }
        } while(true);
        j_jsonlistener.func_27203_g();
    }

    private void func_27449_c(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
        throws J_InvalidSyntaxException, IOException
    {
        char c = (char)func_27448_l(j_positiontrackingpushbackreader);
        if('"' != c)
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object identifier to begin with [\"] but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
        j_positiontrackingpushbackreader.func_27334_a(c);
        j_jsonlistener.func_27205_a(func_27452_i(j_positiontrackingpushbackreader));
        char c1 = (char)func_27448_l(j_positiontrackingpushbackreader);
        if(c1 != ':')
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object identifier to be followed by : but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
        } else
        {
            func_27464_d(j_positiontrackingpushbackreader, j_jsonlistener);
            j_jsonlistener.func_27199_h();
            return;
        }
    }

    private void func_27464_d(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
        throws J_InvalidSyntaxException, IOException
    {
        char c = (char)func_27448_l(j_positiontrackingpushbackreader);
        switch(c)
        {
        case 34: // '"'
            j_positiontrackingpushbackreader.func_27334_a(c);
            j_jsonlistener.func_27198_c(func_27452_i(j_positiontrackingpushbackreader));
            break;

        case 116: // 't'
            char ac[] = new char[3];
            int i = j_positiontrackingpushbackreader.func_27336_b(ac);
            if(i != 3 || ac[0] != 'r' || ac[1] != 'u' || ac[2] != 'e')
            {
                j_positiontrackingpushbackreader.func_27335_a(ac);
                throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 't' to be followed by [[r, u, e]], but got [").append(Arrays.toString(ac)).append("].").toString(), j_positiontrackingpushbackreader);
            }
            j_jsonlistener.func_27196_i();
            break;

        case 102: // 'f'
            char ac1[] = new char[4];
            int j = j_positiontrackingpushbackreader.func_27336_b(ac1);
            if(j != 4 || ac1[0] != 'a' || ac1[1] != 'l' || ac1[2] != 's' || ac1[3] != 'e')
            {
                j_positiontrackingpushbackreader.func_27335_a(ac1);
                throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 'f' to be followed by [[a, l, s, e]], but got [").append(Arrays.toString(ac1)).append("].").toString(), j_positiontrackingpushbackreader);
            }
            j_jsonlistener.func_27193_j();
            break;

        case 110: // 'n'
            char ac2[] = new char[3];
            int k = j_positiontrackingpushbackreader.func_27336_b(ac2);
            if(k != 3 || ac2[0] != 'u' || ac2[1] != 'l' || ac2[2] != 'l')
            {
                j_positiontrackingpushbackreader.func_27335_a(ac2);
                throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 'n' to be followed by [[u, l, l]], but got [").append(Arrays.toString(ac2)).append("].").toString(), j_positiontrackingpushbackreader);
            }
            j_jsonlistener.func_27202_k();
            break;

        case 45: // '-'
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
            j_positiontrackingpushbackreader.func_27334_a(c);
            j_jsonlistener.func_27201_b(func_27459_a(j_positiontrackingpushbackreader));
            break;

        case 123: // '{'
            j_positiontrackingpushbackreader.func_27334_a(c);
            func_27453_b(j_positiontrackingpushbackreader, j_jsonlistener);
            break;

        case 91: // '['
            j_positiontrackingpushbackreader.func_27334_a(c);
            func_27455_a(j_positiontrackingpushbackreader, j_jsonlistener);
            break;

        default:
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Invalid character at start of value [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
    }

    private String func_27459_a(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if('-' == c)
        {
            stringbuilder.append('-');
        } else
        {
            j_positiontrackingpushbackreader.func_27334_a(c);
        }
        stringbuilder.append(func_27451_b(j_positiontrackingpushbackreader));
        return stringbuilder.toString();
    }

    private String func_27451_b(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if('0' == c)
        {
            stringbuilder.append('0');
            stringbuilder.append(func_27462_f(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27454_g(j_positiontrackingpushbackreader));
        } else
        {
            j_positiontrackingpushbackreader.func_27334_a(c);
            stringbuilder.append(func_27460_c(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27456_e(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27462_f(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27454_g(j_positiontrackingpushbackreader));
        }
        return stringbuilder.toString();
    }

    private char func_27460_c(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        char c1 = (char)j_positiontrackingpushbackreader.func_27333_c();
        char c;
        switch(c1)
        {
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
            c = c1;
            break;

        default:
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a digit 1 - 9 but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
        }
        return c;
    }

    private char func_27458_d(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        char c1 = (char)j_positiontrackingpushbackreader.func_27333_c();
        char c;
        switch(c1)
        {
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
            c = c1;
            break;

        default:
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a digit 1 - 9 but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
        }
        return c;
    }

    private String func_27456_e(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = false;
        do
        {
            while(!flag) 
            {
                char c = (char)j_positiontrackingpushbackreader.func_27333_c();
                switch(c)
                {
                case 48: // '0'
                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                    stringbuilder.append(c);
                    break;

                default:
                    flag = true;
                    j_positiontrackingpushbackreader.func_27334_a(c);
                    break;
                }
            }
            return stringbuilder.toString();
        } while(true);
    }

    private String func_27462_f(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if(c == '.')
        {
            stringbuilder.append('.');
            stringbuilder.append(func_27458_d(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27456_e(j_positiontrackingpushbackreader));
        } else
        {
            j_positiontrackingpushbackreader.func_27334_a(c);
        }
        return stringbuilder.toString();
    }

    private String func_27454_g(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if(c == '.' || c == 'E')
        {
            stringbuilder.append('E');
            stringbuilder.append(func_27461_h(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27458_d(j_positiontrackingpushbackreader));
            stringbuilder.append(func_27456_e(j_positiontrackingpushbackreader));
        } else
        {
            j_positiontrackingpushbackreader.func_27334_a(c);
        }
        return stringbuilder.toString();
    }

    private String func_27461_h(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if(c == '+' || c == '-')
        {
            stringbuilder.append(c);
        } else
        {
            j_positiontrackingpushbackreader.func_27334_a(c);
        }
        return stringbuilder.toString();
    }

    private String func_27452_i(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws J_InvalidSyntaxException, IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        char c = (char)j_positiontrackingpushbackreader.func_27333_c();
        if('"' != c)
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected [\"] but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
        }
        boolean flag = false;
        do
        {
            if(flag)
            {
                break;
            }
            char c1 = (char)j_positiontrackingpushbackreader.func_27333_c();
            switch(c1)
            {
            case 34: // '"'
                flag = true;
                break;

            case 92: // '\\'
                char c2 = func_27457_j(j_positiontrackingpushbackreader);
                stringbuilder.append(c2);
                break;

            default:
                stringbuilder.append(c1);
                break;
            }
        } while(true);
        return stringbuilder.toString();
    }

    private char func_27457_j(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        char c1 = (char)j_positiontrackingpushbackreader.func_27333_c();
        char c;
        switch(c1)
        {
        case 34: // '"'
            c = '"';
            break;

        case 92: // '\\'
            c = '\\';
            break;

        case 47: // '/'
            c = '/';
            break;

        case 98: // 'b'
            c = '\b';
            break;

        case 102: // 'f'
            c = '\f';
            break;

        case 110: // 'n'
            c = '\n';
            break;

        case 114: // 'r'
            c = '\r';
            break;

        case 116: // 't'
            c = '\t';
            break;

        case 117: // 'u'
            c = (char)func_27450_k(j_positiontrackingpushbackreader);
            break;

        default:
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Unrecognised escape character [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
        }
        return c;
    }

    private int func_27450_k(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException, J_InvalidSyntaxException
    {
        char ac[] = new char[4];
        int i = j_positiontrackingpushbackreader.func_27336_b(ac);
        if(i != 4)
        {
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a 4 digit hexidecimal number but got only [").append(i).append("], namely [").append(String.valueOf(ac, 0, i)).append("].").toString(), j_positiontrackingpushbackreader);
        }
        int j;
        try
        {
            j = Integer.parseInt(String.valueOf(ac), 16);
        }
        catch(NumberFormatException numberformatexception)
        {
            j_positiontrackingpushbackreader.func_27335_a(ac);
            throw new J_InvalidSyntaxException((new StringBuilder()).append("Unable to parse [").append(String.valueOf(ac)).append("] as a hexidecimal number.").toString(), numberformatexception, j_positiontrackingpushbackreader);
        }
        return j;
    }

    private int func_27448_l(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
        throws IOException
    {
        boolean flag = false;
        int i;
        do
        {
            i = j_positiontrackingpushbackreader.func_27333_c();
            switch(i)
            {
            default:
                flag = true;
                break;

            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                break;
            }
        } while(!flag);
        return i;
    }
}
