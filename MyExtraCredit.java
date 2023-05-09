import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyExtraCredit implements ActionListener {
    JFrame frame;
    JLabel lbIm1;
    JLabel lbIm2;
    BufferedImage img;
    BufferedImage new_img;
    static int width = 512;
    static int height = 512;
    int new_width = 512;
    int new_height = 512;

    int num_lines;
    double rotation_speed;
    double fps;
    double scaling_para;
    boolean anti_aliasing;
    int curr_left = 0;
    int curr_right = 0;
    int sample_period = 0;
    double update_degree;
    long curr_sample_time = 0;
    long prev_time = 0;



    public void drawLine(BufferedImage image, double x1, double y1, double x2, double y2) {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.drawLine((int) Math.ceil(x1), (int) Math.ceil(y1), (int) Math.ceil(x2), (int) Math.ceil(y2));
        g.drawImage(image, 0, 0, null);
    }

    private void drawWhite(BufferedImage img, int width, int height) {
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                byte r = (byte) 255;
                byte g = (byte) 255;
                byte b = (byte) 255;

                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                img.setRGB(x,y,pix);
            }
        }
    }

    private void drawBoundary(BufferedImage img, int width, int height) {
        drawLine(img, 0, 0, width -1, 0);				// top edge
        drawLine(img, 0, 0, 0, height -1);				// left edge
        drawLine(img, 0, height -1, width -1, height -1);	// bottom edge
        drawLine(img, width -1, height -1, width -1, 0); 	// right edge
    }

    private void drawSpecialLines(BufferedImage image, double x1, double y1, double width, double height, double angle) {
        System.out.printf("drawSpecialLines: Drawing the special line at angle: %f\n", angle);
        switch ((int) angle) {
            case 0: {
                drawLine(image, x1, y1, width - 1 , height / 2);
            }
            break;
            case 45: {
                drawLine(image, x1, y1, width - 1, 0);
            }
            break;
            case 90: {
                drawLine(image, x1, y1, width / 2, 0);
            }
            break;
            case 135: {
                drawLine(image, x1, y1, 0, 0);
            }
            break;
            case 180: {
                drawLine(image, x1, y1, 0, height / 2);
            }
            break;
            case 225: {
                drawLine(image, x1, y1, 0, height - 1);
            }
            break;
            case 270: {
                drawLine(image, x1, y1, width / 2, height - 1);
            }
            break;
            case 315: {
                drawLine(image, x1, y1, width - 1, height - 1);
            }
            break;
            case 360: {
                drawLine(image, x1, y1, width - 1 , height / 2);
            }
            break;
            default: {
                System.out.println("drawSpokes: Switch Case default executed. Something wrong with the angle value passed");
            }
            break;
        }
    }

    private void drawImage(BufferedImage image, double x1, double y1, double width, double height, int cur) {
        for (int i = 0; i < num_lines; i++) {
            double angle = (360.00 / num_lines * i + cur) % 360;
            double radian;
            double x2 = width / 2, y2 = height / 2;
            System.out.printf("Drawing Line at angle %f: %d\n", angle, i);
            if (angle % 45 == 0) {
                drawSpecialLines(image, x1, y1, width, height, Math.round(angle));
            } else {
                if (angle > 0 && angle < 45) {
                    radian = Math.toRadians(angle);
                    x2 = width - 1;
                    y2 = height / 2 - Math.tan(radian) * width / 2;
                } else if (angle > 45 && angle < 90) {
                    radian = Math.toRadians(90-angle);
                    x2 = width / 2 + Math.tan(radian) * height / 2;
                    y2 = 0;
                } else if (angle > 90 && angle < 135) {
                    radian = Math.toRadians(angle-90);
                    x2 = width / 2 - Math.tan(radian) * height / 2;
                    y2 = 0;
                } else if (angle > 135 && angle < 180) {
                    radian = Math.toRadians(180-angle);
                    x2 = 0;
                    y2 = height / 2 - Math.tan(radian) * width / 2;
                }else if (angle > 180 && angle < 225) {
                    radian = Math.toRadians(angle-180);
                    x2 = 0;
                    y2 = height / 2 + Math.tan(radian) * width / 2;
                } else if (angle > 225 && angle < 270) {
                    radian = Math.toRadians(270-angle);
                    x2 = width / 2 - Math.tan(radian) * height / 2;
                    y2 = height - 1;
                } else if (angle > 270 && angle < 315) {
                    radian = Math.toRadians(angle-270);
                    x2 = width / 2 + Math.tan(radian) * height / 2;
                    y2 = height - 1;
                } else if (angle > 315 && angle < 360) {
                    radian = Math.toRadians(360-angle);
                    x2 = width - 1;
                    y2 = height / 2 + Math.tan(radian) * width / 2;
                }else {
                    System.out.println("Not Sure where to draw this line");
                }
                System.out.printf("x2: %f | y2: %f\n", x2, y2);
                drawLine(image,x1, y1, x2, y2);
            }
        }
    }

    public BufferedImage scaleImage(BufferedImage originalImage, double scale, boolean antiAlias) {
        if (scale <= 0.0 || (scale == 1 && !antiAlias))
            return originalImage;

        int nWidth = (int) (width / scale), nHeight = (int) (height / scale);
        BufferedImage newImg = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_RGB);
        drawWhite(newImg, nWidth, nHeight);
        for (int y = 0; y < nHeight; y++)
            for (int x = 0; x < nWidth; x++) {
                int color = originalImage.getRGB((int)(x * scale), (int)(y * scale));
                newImg.setRGB(x, y, color);
            }
        return newImg;
    }

    private BufferedImage filterImg(BufferedImage image, int width, int height) {
        System.out.println("filterImg: Applying low pass filter on the image");
        BufferedImage filtered_img = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        for (int y = 1; y < height - 1; y++)
            for (int x = 1; x < width - 1; x++) {
                int r = 0, g = 0, b = 0;
                int[] XDirection = {-1, 0, 1};
                int[] YDirection = {-1, 0, 1};
                for (int dX : XDirection)
                    for (int dY : YDirection) {
                        int tC = image.getRGB(dX + x, dY + y);
                        r += (tC >> 16) & 0xff;
                        g += (tC >> 8) & 0xff;
                        b += (tC) & 0xff;
                    }
                int color = 0xff000000 | (((r / 9) & 0xff) << 16) | (((g / 9) & 0xff) << 8) | ((b / 9) & 0xff);
                filtered_img.setRGB(x, y, color);
            }
        return filtered_img;
    }

    public void showIms() {
        // calculate the new/scaled width and height
        System.out.println("Calculating New Image Width and Height");
        if (this.scaling_para > 0 & this.scaling_para <= 1) {
            this.new_width = (int) Math.ceil(width * this.scaling_para);
            this.new_height = (int) Math.ceil(height * this.scaling_para);
            System.out.printf("New Scaled Image Width: %d Height: %d%n", this.new_width, this.new_height);
        } else {
            System.out.println("Invalid Scaling Factor");
        }

        // Initialize a plain white images
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        new_img = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);

        // deal with the original image
        drawWhite(img, width, height);
        drawBoundary(img, width, height);
        drawImage(img, (double)width / 2, (double)height / 2, width, height, 0);

        // deal with the new image
        drawWhite(new_img, new_width, new_height);
        drawBoundary(new_img, new_width, new_height);
        drawImage(new_img, (double)new_width / 2, (double)new_height / 2, new_width, new_height, 0);
        if(anti_aliasing)
            new_img = filterImg(new_img, new_width, new_height);

        // Use labels to display the images
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        System.out.println("Finished Processing Image");

        JLabel lbText1 = new JLabel("Original Video (Left)");
        lbText1.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lbText2;
        if(anti_aliasing){
            lbText2 = new JLabel("Video after modification (Right) with anti-aliasing filter");
        } else{
            lbText2 = new JLabel("Video after modification (Right) without anti-aliasing filter");
        }
        lbText2.setHorizontalAlignment(SwingConstants.CENTER);

        lbIm1 = new JLabel(new ImageIcon(img));
        lbIm2 = new JLabel(new ImageIcon(new_img));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        frame.getContentPane().add(lbText1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        frame.getContentPane().add(lbText2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        frame.getContentPane().add(lbIm2, c);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    ////
    //	Method called at every timer interval fired
    ////
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean update_sample = false;
        long curr_time = System.currentTimeMillis();
        curr_left = (curr_left - (int)(update_degree * (curr_time - prev_time)) + 360) % 360;   // convert anticlockwise degree to clockwise degree
        if(curr_left < 0)
            curr_left += 360;
        prev_time = System.currentTimeMillis();

        if (curr_time - curr_sample_time > sample_period) {
            curr_sample_time = curr_time;
            update_sample = true;
            curr_left = (curr_right - (int)(update_degree * sample_period) + 360) % 360;
            curr_right = curr_left;
        }
        this.update(img, new_img, curr_left, update_sample);
        frame.repaint();
    }

    public void update(BufferedImage original_image, BufferedImage sample_image, int cur, boolean updateSampleImg) {
        drawWhite(original_image, original_image.getWidth(), original_image.getHeight());
        drawBoundary(original_image, original_image.getWidth(), original_image.getHeight());
        drawImage(original_image, (double)original_image.getWidth() / 2, (double)original_image.getHeight() / 2, original_image.getWidth(), original_image.getHeight(), cur);
        if(updateSampleImg){
            for (int y = 0; y < sample_image.getHeight(); y++) {
                for (int x = 0; x < sample_image.getWidth(); x++) {
                    sample_image.setRGB(x, y, original_image.getRGB((int) (x / scaling_para), (int) (y / scaling_para)));
                }
            }
            drawBoundary(sample_image, sample_image.getWidth(), sample_image.getHeight());
            filterImg(sample_image, sample_image.getWidth(), sample_image.getHeight());
        }
    }

    public static void printSysInfo(){
        System.out.println("printSysInfo: Invalid Usage of the program");
        System.out.println("printSysInfo: Usage Information:");
        System.out.println("printSysInfo: java MyExtraCredit \"Number of Lines\" \"Rotations per second\" \"Frames Per Second\" \"Anti-Aliasing\" \"Scale Factor\"");
        System.exit(1);
    }

    private static int parseNumLines(String NumberOfLines){
        System.out.println("parseNumLines: Parsing the number of lines required to draw");
        int lines = Integer.parseInt(NumberOfLines);
        if (lines > 0){
            return lines;
        }else {
            System.out.println("Invalid Number of lines");
            printSysInfo();
        }
        return 0;
    }

    private static double parseRotationSpeed(String rps){
        System.out.println("parseRotationSpeed: Parsing the rotations per second");
        double rotations = Double.parseDouble(rps);
        if (rotations > 0 & rotations < 20){
            System.out.printf("parseRotationSpeed: Rotations per second: %f\n", rotations);
            return rotations;
        }else{
            System.out.println("parseRotationSpeed: Invalid rotation speed");
            printSysInfo();
        }
        return 0;
    }

    private static double parseFPS(String fps){
        System.out.println("parseFPS: Parsing Frames Per Second Parameter");
        double frames = Double.parseDouble(fps);
        if(frames > 0){
            System.out.printf("parseFPS: Frame Per Second: %f\n",frames);
            return frames;
        }else{
            printSysInfo();
        }
        return 0;
    }

    // parse the antialiasing parameter from command input (String[] args)
    private static boolean parseAntialiasing(String antialiasing){
        System.out.println("parseAntialiasing: Parsing Anti-Aliasing Boolean");
        int aliasing = Integer.parseInt(antialiasing);
        if((aliasing == 0) | (aliasing == 1)){
            if (aliasing == 0){
                System.out.println("parseAntialiasing: Skipping anti-aliasing");
                return false;
            }else{
                System.out.println("parseAntialiasing: Applying anti-aliasing");
                return true;
            }
        }else{
            printSysInfo();
        }
        return false;
    }

    private static double parseScalingPara(String scaling_para){
        System.out.println("parseScalingPara: Calculating New Image Width and Height");
        double scaling = Double.parseDouble(scaling_para);
        if (scaling > 0 & scaling <= 1){
            System.out.printf("parseScalingPara: New Scaled Image Width: %d Height: %d%n",Math.round(width * scaling), Math.round(height * scaling));
            return scaling;
        }else{
            System.out.println("parseScalingPara: Invalid Scaling Factor since the image will be bigger then the original image or way to small to render");
            printSysInfo();
        }
        return 0;
    }

    public static void main(String[] args) {
        if (args.length == 5){
            MyExtraCredit ren = new MyExtraCredit();
            ren.num_lines = parseNumLines(args[0]);
            ren.rotation_speed = parseRotationSpeed(args[1]);
            ren.fps = parseFPS(args[2]);
            ren.anti_aliasing = parseAntialiasing(args[3]);
            ren.scaling_para = parseScalingPara(args[4]);
            ren.sample_period = (int)(1000/ren.fps);
            ren.update_degree = (360 * ren.rotation_speed) / 1000;
            ren.showIms();
            Timer timer = new Timer(50,ren);
            timer.start();
        }else {
            printSysInfo();
        }
    }
}